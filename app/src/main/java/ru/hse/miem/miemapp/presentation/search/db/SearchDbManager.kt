package ru.hse.miem.miemapp.presentation.search.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.data.repositories.withIO
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch

class SearchDbManager(context: Context) {
    private val dbHelper = SearchDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun insertDb(
        id: Long,
        number: Long,
        name: String,
        type: String,
        state: String,
        isActive: Boolean,
        vacancies: Int,
        head: String
    ) = CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, e ->
        println(e.message)
    }).launch {
        val values = ContentValues().apply {
            put(SearchDbClass.COLUMN_NAME_PROJECT_ID, id)
            put(SearchDbClass.COLUMN_NAME_NUMBER, number)
            put(SearchDbClass.COLUMN_NAME_PROJECT_NAME, name)
            put(SearchDbClass.COLUMN_NAME_TYPE, type)
            put(SearchDbClass.COLUMN_NAME_STATE, state)
            put(SearchDbClass.COLUMN_NAME_IS_ACTIVE, if (isActive) 1 else 0)
            put(SearchDbClass.COLUMN_NAME_VACANCIES, vacancies)
            put(SearchDbClass.COLUMN_NAME_HEAD, head)
        }

        db?.insert(SearchDbClass.TABLE_NAME, null, values)
    }

    suspend fun readDb() = withIO {
        val data: MutableList<ProjectInSearch> = mutableListOf()
        val cursor = db?.query(SearchDbClass.TABLE_NAME, null, null, null, null, null, null)

        while (cursor?.moveToNext()!!) {
            val id =
                cursor.getLong(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_PROJECT_ID))
            val number =
                cursor.getLong(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_NUMBER))
            val name =
                cursor.getString(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_PROJECT_NAME))
            val type =
                cursor.getString(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_TYPE))
            val state =
                cursor.getString(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_STATE))
            val isActive =
                cursor.getInt(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_IS_ACTIVE))
            val vacancies =
                cursor.getInt(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_VACANCIES))
            val head =
                cursor.getString(cursor.getColumnIndex(SearchDbClass.COLUMN_NAME_HEAD))
            data.add(
                ProjectInSearch(
                    id = id,
                    number = number,
                    name = name,
                    type = type,
                    state = state,
                    isActive = isActive == 1,
                    vacancies = vacancies,
                    head = head
                )
            )
        }
        cursor.close()

        try {
            dbHelper.onUpgrade(db, 1, 1)
        } catch (e: Exception) {}

        data
    }

    fun closeDb() {
        dbHelper.close()
    }

    fun deleteDb() {
        db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 2, 2)
        dbHelper.close()
    }

    fun upgradeDb() {
        dbHelper.onUpgrade(db, 1, 1)
    }
}