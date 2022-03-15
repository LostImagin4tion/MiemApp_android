package ru.hse.miem.miemapp.domain.entities

import ru.hse.miem.miemapp.data.api.ScheduleResponse

data class Profile(
    val id: Long,
    val isTeacher: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String,
    val occupation: String,
    val avatarUrl: String,
    val chatUrl: String
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
data class ScheduleDay(
    val date: String,
    val dayOfWeek: String,
    val lessons: MutableList<ScheduleResponse.Data>
)