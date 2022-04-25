package ru.hse.miem.miemapp.presentation.schedule.db

import android.provider.BaseColumns

object ScheduleDbClass: BaseColumns {
    const val TABLE_NAME = "scheduleDb"

    const val COLUMN_NAME_TYPE = "type"
    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_NAME_DAY_OF_WEEK = "dayOfWeek"

    const val COLUMN_NAME_AUDITORIUM = "auditorium"
    const val COLUMN_NAME_BEGIN_LESSON = "beginLesson"
    const val COLUMN_NAME_END_LESSON = "endLesson"
    const val COLUMN_NAME_LESSON_NUMBER = "lessonNumber"
    const val COLUMN_NAME_ADDRESS = "address"
    const val COLUMN_NAME_DISCIPLINE = "discipline"
    const val COLUMN_NAME_KIND_OF_LESSON = "kindOfLesson"
    const val COLUMN_NAME_LECTURER = "lecturer"

    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "cache.db"

    const val CREATE_TABLE = "create table if not exists $TABLE_NAME(" +
            "${BaseColumns._ID} integer primary key," +
            "$COLUMN_NAME_TYPE TEXT, $COLUMN_NAME_DATE TEXT," +
            "$COLUMN_NAME_DAY_OF_WEEK TEXT, $COLUMN_NAME_AUDITORIUM TEXT," +
            "$COLUMN_NAME_BEGIN_LESSON TEXT, $COLUMN_NAME_END_LESSON TEXT" +
            "$COLUMN_NAME_LESSON_NUMBER TEXT, $COLUMN_NAME_ADDRESS TEXT" +
            "$COLUMN_NAME_DISCIPLINE TEXT, $COLUMN_NAME_KIND_OF_LESSON TEXT" +
            "$COLUMN_NAME_LECTURER TEXT)"

    const val DELETE_TABLE = "drop table if exists $TABLE_NAME"
}