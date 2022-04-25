package ru.hse.miem.miemapp.presentation.tinder.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import java.lang.Exception
import java.util.ArrayList

class DbManager(context: Context) {
    private val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun insertDb(
        project_id: String,
        project_name_rus: String,
        vacancy_role: String,
        requirements: String
    ) {
        val values = ContentValues().apply {
            put(TinderDbClass.COLUMN_NAME_ROLE, vacancy_role)
            put(TinderDbClass.COLUMN_NAME_REQUIREMENTS, requirements)
            put(TinderDbClass.COLUMN_NAME_PROJECT_ID, project_id)
            put(TinderDbClass.COLUMN_NAME_PROJECT_NAME, project_name_rus)
        }

        db?.insert(TinderDbClass.TABLE_NAME, null, values)
    }

    fun readDb(): ArrayList<VacancyCard> {
        val data: ArrayList<VacancyCard> = arrayListOf()
        val cursor = db?.query(TinderDbClass.TABLE_NAME, null, null, null, null, null, null)

        while (cursor?.moveToNext()!!) {
            val project_id =
                cursor.getString(cursor.getColumnIndex(TinderDbClass.COLUMN_NAME_PROJECT_ID))
            val name =
                cursor.getString(cursor.getColumnIndex(TinderDbClass.COLUMN_NAME_PROJECT_NAME))
            val role = cursor.getString(cursor.getColumnIndex(TinderDbClass.COLUMN_NAME_ROLE))
            val requirements =
                cursor.getString(cursor.getColumnIndex(TinderDbClass.COLUMN_NAME_REQUIREMENTS))
            data.add(VacancyCard(project_id, name, role, requirements))
        }
        cursor.close()

        try {
            dbHelper.onUpgrade(db, 1, 1)
        } catch (exception: Exception) {}

        return data
    }

    fun closeDb() {
        dbHelper.close()
    }

    fun deleteDb(){
        db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 2, 2)
        dbHelper.close()
    }
}