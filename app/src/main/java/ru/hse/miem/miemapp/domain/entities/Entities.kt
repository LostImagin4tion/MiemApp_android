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

data class ProjectBasic(
    val id: Long,
    val number: Long,
    val name: String,
    val members: Int
)