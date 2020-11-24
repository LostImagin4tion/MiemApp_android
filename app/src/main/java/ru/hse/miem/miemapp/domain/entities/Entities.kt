package ru.hse.miem.miemapp.domain.entities

data class Profile(
    val id: Long,
    val isTeacher: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String,
    val occupation: String,
    val avatarUrl: String
)

// displayed in profile
data class ProjectBasic(
    val id: Long,
    val number: Long,
    val name: String,
    val members: Int
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
    val links: List<Link>
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