package ru.hse.miem.miemapp.presentation.tinder.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.lang.Exception

class DbManager(context: Context) {
    private val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = dbHelper.writableDatabase
    }

    fun insertDb(role: String, amount: Int){
        val values = ContentValues().apply {
            put(TinderDbClass.COLUMN_NAME_ROLE, role)
            put(TinderDbClass.COLUMN_NAME_COUNT, amount)
        }

        db?.insert(TinderDbClass.TABLE_NAME, null, values)
    }

    fun readDb(): MutableMap<String, Int>{
        val data: MutableMap<String, Int> = mutableMapOf()
        val cursor = db?.query(TinderDbClass.TABLE_NAME, null, null, null, null, null, null)

        while (cursor?.moveToNext()!!){
            val role = cursor.getString(cursor.getColumnIndex(TinderDbClass.COLUMN_NAME_ROLE))
            val amount = cursor.getInt(cursor.getColumnIndex(TinderDbClass.COLUMN_NAME_COUNT))
            data[role] = amount
        }
        cursor.close()
        try {
            dbHelper.onUpgrade(db, 1, 1)
        }catch (exception: Exception){}
        return data
    }

    fun closeDb(){
        dbHelper.close()
    }
}