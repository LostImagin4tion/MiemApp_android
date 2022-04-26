package ru.hse.miem.miemapp.presentation.schedule.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.data.api.ScheduleResponse
import ru.hse.miem.miemapp.data.repositories.withIO
import ru.hse.miem.miemapp.domain.entities.IScheduleItem
import ru.hse.miem.miemapp.domain.entities.ScheduleDayLesson
import ru.hse.miem.miemapp.domain.entities.ScheduleDayName

class ScheduleDbManager(context: Context) {
    private val dbHelper = ScheduleDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun insertDb(
        type: String,
        date: String,
        dayOfWeek: String,
        auditorium: String? = null,
        beginLesson: String? = null,
        endLesson: String? = null,
        lessonNumber: String? = null,
        address: String? = null,
        discipline: String? = null,
        kindOfLesson: String? = null,
        lecturer: String? = null
    ) {
        val values = ContentValues().apply {
            put(ScheduleDbClass.COLUMN_NAME_TYPE, type)
            put(ScheduleDbClass.COLUMN_NAME_DATE, date)
            put(ScheduleDbClass.COLUMN_NAME_DAY_OF_WEEK, dayOfWeek)
            put(ScheduleDbClass.COLUMN_NAME_AUDITORIUM, auditorium)
            put(ScheduleDbClass.COLUMN_NAME_BEGIN_LESSON, beginLesson)
            put(ScheduleDbClass.COLUMN_NAME_END_LESSON, endLesson)
            put(ScheduleDbClass.COLUMN_NAME_LESSON_NUMBER, lessonNumber)
            put(ScheduleDbClass.COLUMN_NAME_ADDRESS, address)
            put(ScheduleDbClass.COLUMN_NAME_DISCIPLINE, discipline)
            put(ScheduleDbClass.COLUMN_NAME_KIND_OF_LESSON, kindOfLesson)
            put(ScheduleDbClass.COLUMN_NAME_LECTURER, lecturer)
        }

        db?.insert(ScheduleDbClass.TABLE_NAME, null, values)
    }

    suspend fun readDb() = withIO {
        val data: MutableList<IScheduleItem> = mutableListOf()
        val cursor = db?.query(ScheduleDbClass.TABLE_NAME, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!) {
            val type =
                cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_TYPE))
            val date =
                cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_DATE))
            val dayOfWeek =
                cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_DAY_OF_WEEK))

            if (type == "day") {
                data.add(ScheduleDayName(
                    date = date,
                    dayOfWeek = dayOfWeek
                ))
            }
            else {
                val auditorium =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_AUDITORIUM))
                val beginLesson =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_BEGIN_LESSON))
                val endLesson =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_END_LESSON))
                val lessonNumber =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_LESSON_NUMBER))
                val address =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_ADDRESS))
                val discipline =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_DISCIPLINE))
                val kindOfLesson =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_KIND_OF_LESSON))
                val lecturer =
                    cursor.getString(cursor.getColumnIndex(ScheduleDbClass.COLUMN_NAME_LECTURER))
                data.add(ScheduleDayLesson(
                    date = date,
                    dayOfWeek = dayOfWeek,
                    lesson = ScheduleResponse(
                        auditorium = auditorium ?: "",
                        beginLesson = beginLesson ?: "",
                        endLesson = endLesson ?: "",
                        lessonNumberStart = lessonNumber ?: "",
                        building = address ?: "",
                        date = date ?: "",
                        discipline = discipline ?: "",
                        kindOfWork = kindOfLesson ?: "",
                        lecturer = lecturer ?: ""
                    )
                ))
            }
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