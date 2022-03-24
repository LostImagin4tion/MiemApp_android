package ru.hse.miem.miemapp.presentation.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.data.api.ScheduleResponse
import java.util.*

class ScheduleViewHolderFactory {

    fun create(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {

        return when(type) {
            0-> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_day_in_schedule, parent, false)

                DayViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_class_in_schedule, parent, false)

                LessonViewHolder(view)
            }
            else -> throw IllegalStateException()
        }
    }

    class DayViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val dayNameView = itemView.findViewById<TextView>(R.id.dayInSchedule)

        fun bind(dayOfWeek: String) {
            dayNameView.text = dayOfWeek
        }
    }

    class LessonViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val lessonStart = itemView.findViewById<TextView>(R.id.classStart)
        private val lessonFinish = itemView.findViewById<TextView>(R.id.classFinish)
        private val lessonNumber = itemView.findViewById<TextView>(R.id.classNumber)

        private val lessonName = itemView.findViewById<TextView>(R.id.className)
        private val lessonType = itemView.findViewById<TextView>(R.id.classType)

        private val lessonAddress = itemView.findViewById<TextView>(R.id.classAddress)
        private val lessonTeacher = itemView.findViewById<TextView>(R.id.classTeacher)

        fun bind(lesson: ScheduleResponse) = itemView.apply {

            lessonStart.text = lesson.beginLesson
            lessonFinish.text = lesson.endLesson
            lessonNumber.text = context.getString(R.string.class_number).format(lesson.lessonNumberStart)
            lessonName.text = lesson.discipline
            lessonType.text = lesson.kindOfWork
            lessonAddress.text = lesson.building
            lessonTeacher.text = lesson.lecturer
            lessonTeacher.text = when(Locale.getDefault().displayLanguage) {
                "Russian" -> lesson.lecturer
                else -> {
                    val fullName: MutableList<String> = lesson.lecturer!!.split(" ").toMutableList()

                    for(i in fullName.indices) {
                        fullName[i] = fullName[i].lowercase()
                        fullName[i] = fullName[i].replaceFirstChar { it.uppercaseChar() }
                    }

                    fullName.joinToString(" ")
                }
            }
        }
    }
}