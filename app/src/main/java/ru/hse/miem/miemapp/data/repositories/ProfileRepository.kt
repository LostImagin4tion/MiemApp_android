package ru.hse.miem.miemapp.data.repositories

import io.reactivex.Completable
import io.reactivex.Single
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemapp.data.api.ApplicationConfirmRequest
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.StudentProfileResponse
import ru.hse.miem.miemapp.data.api.TeacherProfileResponse
import ru.hse.miem.miemapp.domain.entities.MyProjectsAndApplications
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.domain.entities.ProjectBasic
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val cabinetApi: CabinetApi,
    private val session: Session
) : IProfileRepository {

    override fun getMyProfile() = if (session.isStudent) {
        cabinetApi.myStudentProfile().studentToProfile()
    } else {
        cabinetApi.myTeacherProfile().teacherToProfile()
    }

    override fun getProfileById(id: Long, isTeacher: Boolean) = if(isTeacher) {
        cabinetApi.teacherProfile(id).teacherToProfile()
    } else {
        cabinetApi.studentProfile(id).studentToProfile()
    }

    override fun getProjects(userId: Long) = cabinetApi.userStatistic(userId)
        .map {
            it.data.myProjects.map {
                ProjectBasic(
                    id = it.id,
                    number = it.number,
                    name = it.name,
                    members = it.students
                )
            }
        }

    override fun getMyProjectsAndApplications(): Single<MyProjectsAndApplications> = cabinetApi.myUserStatistic()
        .map {
            val projects = it.data.projects.data.map {
                MyProjectsAndApplications.MyProjectBasic(
                    id = it.project_id,
                    number = it.number,
                    name = it.project_name,
                    members = it.team.size,
                    hours = it.userHours,
                    head = it.leader,
                    type = it.type,
                    role = it.role,
                    state = it.state,
                    isActive = it.statusId == 2
                )
            }

            val applications = it.data.applications.data.map {
                MyProjectsAndApplications.MyApplication(
                    id = it.id,
                    projectId = it.project_id,
                    projectNumber = it.project_name.split(" ")[0].toLong(),
                    projectName = it.project_name,
                    projectType = it.type,
                    role = it.role,
                    head = it.leader,
                    status = MyProjectsAndApplications.MyApplication.Status.valueOf(it.status),
                    studentComment = it.studentComment,
                    headComment = it.leaderComment
                )
            }

            MyProjectsAndApplications(projects, applications)
        }

    private fun Single<StudentProfileResponse>.studentToProfile() = map {
        val main = it.data[0].main
        Profile(
            id = main.studentId,
            isTeacher = false,
            firstName = main.name.split(" ")[1],
            lastName = main.name.split(" ")[0],
            email = main.email,
            occupation = main.group,
            avatarUrl = CabinetApi.getAvatarUrl(main.studentId),
            chatUrl = main.chatLink
        )
    }

    private fun Single<TeacherProfileResponse>.teacherToProfile() = map {
        val main = it.data[0].main
        Profile(
            id = main.teacherId,
            isTeacher = true,
            firstName = main.name.split(" ")[1],
            lastName = main.name.split(" ")[0],
            email = main.email,
            occupation = main.department,
            avatarUrl = CabinetApi.getAvatarUrl(main.teacherId),
            chatUrl = CabinetApi.DEFAULT_CHAT_LINK
        )
    }

    override fun applicationWithdraw(applicationId: Long) = applicationConfirm(applicationId, StudentConfirmAction.WITHDRAW)

    override fun applicationApprove(applicationId: Long) = applicationConfirm(applicationId, StudentConfirmAction.APPROVE)

    private fun applicationConfirm(applicationId: Long, action: StudentConfirmAction) = cabinetApi
        .applicationConfirm(ApplicationConfirmRequest(applicationId, action.status))

    private enum class StudentConfirmAction(val status: Int) {
        WITHDRAW(2),
        APPROVE(1), // TODO check if this is right
    }


}