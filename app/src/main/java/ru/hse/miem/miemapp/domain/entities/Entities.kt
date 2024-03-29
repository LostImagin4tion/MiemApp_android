package ru.hse.miem.miemapp.domain.entities

import androidx.recyclerview.widget.RecyclerView
import ru.hse.miem.miemapp.data.api.ScheduleResponse
import ru.hse.miem.miemapp.presentation.schedule.ScheduleViewHolderFactory

data class Profile(
    val id: Long,
    val isTeacher: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String,
    val occupation: String,
    val avatarUrl: String,
    val chatUrl: String?
)

// displayed in profile
data class ProjectBasic(
    val id: Long,
    val number: Long,
    val name: String,
    val members: Int
)

// displayed in my profile
data class MyProjectsAndApplications(
    val projects: List<MyProjectBasic>,
    val applications: List<MyApplication>
) {
    data class MyProjectBasic(
        val id: Long,
        val number: Long,
        val name: String,
        val members: Int,
        val hours: Int,
        val head: String,
        val type: String,
        val role: String,
        val state: String,
        val isActive: Boolean
    )

    data class MyApplication(
        val id: Long,
        val projectId: Long,
        val projectNumber: Long,
        val projectName: String,
        val projectType: String,
        val role: String,
        val head: String,
        val status: Status,
        val studentComment: String?,
        val headComment: String?
    ) {
        enum class Status(val status: Int) {
            WAITING(0),
            APPROVED(1),
            DECLINED(2);

            companion object {
                fun valueOf(status: Int) = values().find { it.status == status }
                    ?: throw IllegalArgumentException("Unknown status")
            }
        }
    }
}

data class Achievements(
    val tracker: List<Tracker>,
    val gitlab: List<Gitlab>
) {
    data class Tracker(
        val id: Int,
        val name: String,
        val categoryId: Int,
        val awardCondition: String,
        val image: String,
        val progress: Int
    )

    data class Gitlab(
        val id: Int,
        val name: String,
        val categoryId: Int,
        val awardCondition: String,
        val image: String,
        val progress: Int
    )

    enum class Category(val categoryId: Int) {
        TRACKER(1),
        GITLAB(2);

        companion object {
            fun valueOf(category: Int) = values().find { it.categoryId == category }
                ?: throw java.lang.IllegalArgumentException("Unknown category")
        }
    }
}

data class UserGitStatistics(
    val repoId: Long,
    val name: String,
    val commitCount: Int,
    val stringsCount: Int,
    val usedLanguages: List<String>
)

// displayed in project screen
data class ProjectExtended(
    val id: Long,
    val number: Long,
    val name: String,
    val type: String,
    val source: String,
    val isActive: Boolean,
    val state: String,
    val email: String,
    val objective: String,
    val annotation: String,
    val members: List<Member>,
    val links: List<Link>,
    val vacancies: List<Vacancy>,
    val url: String
) {
    data class Member(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val isTeacher: Boolean,
        val role: String,
        val avatarUrl: String
    )

    data class Link(
        val name: String,
        val url: String
    )

    data class Vacancy(
        val id: Long,
        val role: String,
        val required: String,
        val recommended: String,
        val count: Int,
        val isApplied: Boolean
    )
}

// displayed in search
data class ProjectInSearch(
    val id: Long,
    val number: Long,
    val name: String,
    val type: String,
    val state: String,
    val isActive: Boolean,
    val vacancies: Int,
    val head: String
)

data class Vacancies(
    val vacancyId: Long,
    val projectId: Long,
    val projectNameRus: String,
    val vacancyRole: String,
    val vacancyDisciplines: List<String>,
    val vacancyAdditionally: List<String>
)

data class VacancyCard(
    var projectId: String,
    var projectNameRus: String,
    var vacancyRole: String,
    var requirements: String
)

val tagsList = listOf(
    "c++", "python", "kotlin", "java", "arduino", "quartus", "html",
    "php", "android", "design", "git", "linux", "js", "c/c++", "c#", "sql", "sqlite", "docker",
    "css", "ux", "ui", "raspberry", "backend", "frontend", "front-end", "back-end", "http",
    "oracle", "pgsql", "бд", "dns", "dshp", "gpo", "hfss", "awr de", "matlab", "verilog",
    "плис", "autocad", "cst", "физика", "электроника", "api", "google", "labview",
    "bstrap", "go", "сети", "3d", "ios", "swift", "электротехника", "fullstack",
    "full-stack", "delphi"
)

//displayed in schedule
interface IScheduleItem {

    fun getItemViewType(): Int

    fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder)

    companion object {
        const val DAY_CARD_TYPE = 0
        const val LESSON_CARD_TYPE = 1
    }
}

sealed class ScheduleItem

class ScheduleDayName(
    val date: String,
    val dayOfWeek: String
): ScheduleItem(), IScheduleItem {

    override fun getItemViewType(): Int {
        return IScheduleItem.DAY_CARD_TYPE
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as ScheduleViewHolderFactory.DayViewHolder).bind(dayOfWeek)
    }
}

class ScheduleDayLesson(
    val date: String,
    val dayOfWeek: String,
    val lesson: ScheduleResponse
): ScheduleItem(), IScheduleItem {

    override fun getItemViewType(): Int {
        return IScheduleItem.LESSON_CARD_TYPE
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val lessonsHolder = viewHolder as ScheduleViewHolderFactory.LessonViewHolder

        lessonsHolder.bind(lesson)
    }
}

//displayed in sandbox
data class ProjectInSandbox(
    val id: Long,
    val stateId: Int,
    val state: String,
    val name: String,
    val head: String,
    val type: String,
    val vacancies: Int
)