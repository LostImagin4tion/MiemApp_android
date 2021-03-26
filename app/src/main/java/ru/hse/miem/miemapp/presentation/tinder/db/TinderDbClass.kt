package ru.hse.miem.miemapp.presentation.tinder.db

import android.provider.BaseColumns

object TinderDbClass: BaseColumns{
    const val TABLE_NAME = "tinderDb"
    const val COLUMN_NAME_PROJECT_ID = "project_id"
    const val COLUMN_NAME_PROJECT_NAME = "project_name_rus"
    const val COLUMN_NAME_ROLE = "vacancy_role"
    const val COLUMN_NAME_REQUIREMENTS = "requirements"

    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "vacancy.db"

    const val CREATE_TABLE = "create table if not exists $TABLE_NAME(" +
            "${BaseColumns._ID} integer primary key," +
            "$COLUMN_NAME_PROJECT_ID TEXT, $COLUMN_NAME_PROJECT_NAME TEXT," +
            "$COLUMN_NAME_ROLE TEXT, $COLUMN_NAME_REQUIREMENTS TEXT)"

    const val DELETE_TABLE = "delete table if exists $TABLE_NAME"
}