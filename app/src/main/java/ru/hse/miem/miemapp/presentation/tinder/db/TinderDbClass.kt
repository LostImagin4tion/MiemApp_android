package ru.hse.miem.miemapp.presentation.tinder.db

import android.provider.BaseColumns

object TinderDbClass: BaseColumns{
    const val TABLE_NAME = "db_tinder"
    const val COLUMN_NAME_TITLE = "TITLE"
    const val COLUMN_NAME_COUNT = "amount"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "tinder.db"

    const val CREATE_TABLE = "create table if not exists $TABLE_NAME(" +
            "${BaseColumns._ID} integer primary key," +
            "$COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_COUNT integer)"

    const val DELETE_TABLE = "delete table if exists $TABLE_NAME"
}