package ru.hse.miem.miemapp.presentation.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_class_in_schedule.view.*
import kotlinx.android.synthetic.main.item_day_in_schedule.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ScheduleDay

class ScheduleAdapter: RecyclerView.Adapter<ScheduleAdapter.ProjectViewHolder>() {

    private var lessonDays: MutableList<ScheduleDay> = mutableListOf()
    private var displayedDays = lessonDays

    val hasData get() = lessonDays.isNotEmpty()

    fun updateWhenScrolledDown(newLessonDays: List<ScheduleDay>) {
        lessonDays.addAll(newLessonDays)
        displayedDays = lessonDays

        notifyDataSetChanged()
    }

    fun update(days: List<ScheduleDay>) {

        this.lessonDays = if (days.isEmpty()) {
            mutableListOf()
        } else {
            days as MutableList<ScheduleDay>
        }

        displayedDays = this.lessonDays

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_day_in_schedule, parent, false)

        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(displayedDays[position])
    }

    override fun getItemCount() = displayedDays.size

    class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(lessonDay: ScheduleDay) = itemView.apply {

            dayInSchedule.text = lessonDay.dayOfWeek

            val lessons = lessonDay.lessons

            for (i in lessons.indices) {

                scheduleGrid.classStart.text = lessons[i].beginLesson
                scheduleGrid.classFinish.text = lessons[i].endLesson
                scheduleGrid.classNumber.text = context.getString(R.string.class_number).format(lessons[i].lessonNumberStart)
                scheduleGrid.className.text = lessons[i].discipline
                scheduleGrid.classType.text = lessons[i].kindOfWork
                scheduleGrid.classAddress.text = lessons[i].building
                scheduleGrid.classTeacher.text = lessons[i].lecturer
            }
        }
    }
}