package ru.hse.miem.miemapp.presentation.search.db

import android.provider.BaseColumns

object SearchDbClass: BaseColumns {
    const val TABLE_NAME = "searchDb"

    const val COLUMN_NAME_PROJECT_ID = "project_id"
    const val COLUMN_NAME_NUMBER = "number"
    const val COLUMN_NAME_PROJECT_NAME = "name"
    const val COLUMN_NAME_TYPE = "type"
    const val COLUMN_NAME_STATE = "state"
    const val COLUMN_NAME_IS_ACTIVE = "isActive"
    const val COLUMN_NAME_VACANCIES = "vacancies"
    const val COLUMN_NAME_HEAD = "head"

    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "cache.db"

    const val CREATE_TABLE = "create table if not exists $TABLE_NAME(" +
            "${BaseColumns._ID} integer primary key," +
            "$COLUMN_NAME_PROJECT_ID TEXT, $COLUMN_NAME_PROJECT_NAME TEXT," +
            "$COLUMN_NAME_TYPE TEXT, $COLUMN_NAME_STATE TEXT," +
            "$COLUMN_NAME_IS_ACTIVE TEXT, $COLUMN_NAME_VACANCIES TEXT" +
            "$COLUMN_NAME_HEAD TEXT"

    const val DELETE_TABLE = "drop table if exists $TABLE_NAME"
}