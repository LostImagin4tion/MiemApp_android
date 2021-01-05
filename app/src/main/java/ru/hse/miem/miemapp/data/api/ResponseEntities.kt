package ru.hse.miem.miemapp.data.api

/**
 * Auth
 */
data class AuthResponse(
    val data: Data
) {
    data class Data(
        val email: String,
        val full_name: String,
        val token: String
    )
}


/**
 * Profile related
 */
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
        val direction: String,
        val chatLink: String
    )
}

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

data class MyStatisticResponse(
    val data: Data
) {
    data class Data(
        val projects: Projects
    ) {
        data class Projects(
            val data: List<ProjectData>
        ) {
            data class ProjectData(
                val project_id: Long,
                val project_name: String,
                val number: Long,
                val team: List<String>,
                val leader: String,
                val state: String,
                val type: String,
                val role: String,
                val userHours: Int,
                val statusId: Int
            )
        }
    }
}


/**
 * Project related
 */
data class ProjectHeaderResponse(
    val data: Data
) {
    data class Data(
        val id: Long,
        val number: Any?, // here's workaround because api developers are stupid af and sometimes return boolean in number field
        val googleGroup: String?,
        val typeLabel: String,
        val sourceLabel: String,
        val statusLabel: String,
        val statusValue: Int,
        val nameRus: String,
        val trello: String
    )
}

data class ProjectBodyResponse(
    val data: Data
) {
    data class Data(
        val id: Long,
        val target: String?,
        val annotation: String?
    )
}

data class ProjectMembersResponse(
    val data: Data
) {
    data class Data(
        val leaders: List<Member>,
        val activeMembers: List<Member>
    ) {
        data class Member(
            val id: Long,
            val first_name: String?,
            val last_name: String?,
            val name: String,
            val dep: String?,
            val role: String
        )
    }
}

data class ProjectVacanciesResponse(
    val data: List<Data>
) {
    data class Data(
        val vacancy_id: Long,
        val role: String,
        val count: Int,
        val booked: Boolean,
        val disciplines: List<String>,
        val additionally: List<String>
    )
}

data class GitStatisticsResponse(
    val data: List<Data>
) {
    data class Data(
        val summary: List<GitData>
    ) {
        data class GitData(
            val project: String,
            val link: String
        )
    }
}

/**
 * Search related
 */
data class ProjectsAllResponse(
    val data: List<Data>
) {
    data class Data(
        val id: Long,
        val number: Long?,
        val vacancies: Int,
        val nameRus: String,
        val typeDesc: String,
        val statusId: Int,
        val statusDesc: String,
        val head: String
    )
}