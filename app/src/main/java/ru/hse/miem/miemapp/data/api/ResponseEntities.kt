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
        val projects: List<Projects>,
        val applications: List<Applications>,
        val approved_applications: List<Applications>
    ) {
        data class Projects(
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

        data class Applications(
            val applId: Long,
            val head: String,
            val leaderComment: String?,
            val studentComment: String?,
            val type: String,
            val nameRus: String,
            val id: Long,
            val role: String,
            val status: Int
        )
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
    val data: List<Data>,
    val code: Int
) {
    data class Data(
        val vacancy_id: Long,
        val role: String,
        val count: Int,
        val booked: Boolean,
        val applied: Boolean,
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

data class VacanciesAllResponse(
    val data: List<Data>
){
    data class Data(
        val vacancy_id: Long?,
        val project_id: Long?,
        val project_name_rus: String?,
        val vacancy_role: String?,
        val vacancy_disciplines: List<String>?,
        val vacancy_additionally: List<String>?
    )
}

/**
 * Schedule related
 */
data class ScheduleResponse (
    val auditorium: String?,
    val beginLesson: String,
    val endLesson: String,
    val lessonNumberStart: String,
    val building: String?,
    val date: String,
    val discipline: String,
    val group: String?,
    val kindOfWork: String,
    val lecturer: String?
)

data class UserInfoResponse (
    val data: Data
) {
    data class Data(
        val userId: Int,
        val fullName: String,
        val emailsList: List<String>,
        val isTeacher: Boolean
    )
}

/**
 * Sandbox related
 */
data class SandboxResponse(
    val data: List<Data>
) {
    data class Data(
        val id: Long,
        val statusId: Int,
        val statusDesc: String,
        val nameRus: String,
        val head: String,
        val typeDesc: String,
        val vacancies: Int,
    )
}