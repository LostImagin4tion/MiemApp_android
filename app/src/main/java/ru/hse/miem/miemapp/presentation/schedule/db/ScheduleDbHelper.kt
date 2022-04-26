package ru.hse.miem.miemapp.presentation.schedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScheduleDbHelper(context: Context):
    SQLiteOpenHelper(context, ScheduleDbClass.DATABASE_NAME, null, ScheduleDbClass.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(ScheduleDbClass.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(ScheduleDbClass.DELETE_TABLE)
        onCreate(db)
    }
}