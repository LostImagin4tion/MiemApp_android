package ru.hse.miem.miemapp.presentation.search.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SearchDbHelper(context: Context):
    SQLiteOpenHelper(context, SearchDbClass.DATABASE_NAME, null, SearchDbClass.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SearchDbClass.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SearchDbClass.DELETE_TABLE)
        onCreate(db)
    }
}