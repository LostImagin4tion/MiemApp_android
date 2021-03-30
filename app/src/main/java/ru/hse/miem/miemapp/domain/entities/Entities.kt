package ru.hse.miem.miemapp.domain.entities

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
        val count: Int
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
    val vacancy_id: Long,
    val project_id: Long,
    val project_name_rus: String,
    val vacancy_role: String,
    val vacancy_disciplines: List<String>,
    val vacancy_additionally: List<String>
)

data class VacancyCard(
    var project_id: String,
    var project_name_rus: String,
    var vacancy_role: String,
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
