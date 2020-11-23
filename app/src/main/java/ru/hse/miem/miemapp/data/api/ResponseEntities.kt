package ru.hse.miem.miemapp.data.api

data class AuthResponse(
    val data: Data
) {
    data class Data(
        val email: String,
        val full_name: String,
        val token: String
    )
}

data class StudentProfileResponse(
    val data: List<Data>
) {
    data class Data(
        val main: Main
    )

    data class Main(
        val studentId: Long,
        val name: String,
        val group: String,
        val email: String,
        val direction: String
    )
}

// TODO test if it actually works
data class TeacherProfileResponse(
    val data: List<Data>
) {
    data class Data(
        val main: Main
    )

    data class Main(
        val teacherId: Long,
        val name: String,
        val department: String,
        val email: String
    )
}

data class StatisticResponse(
    val data: Data
) {
    data class Data(
        val myProjects: List<Project>
    ) {
        data class Project(
            val id: Long,
            val name: String,
            val number: Long,
            val students: Int
        )
    }
}