package ru.hse.miem.miemapp.data.repositories

import kotlinx.coroutines.async
import ru.hse.miem.miemapp.Session
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


    override suspend fun getMyProfile() = withIO {
        if (session.isStudent) {
            cabinetApi.myStudentProfile().studentToProfile()
        } else {
            cabinetApi.myTeacherProfile().teacherToProfile()
        }
    }

    override suspend fun getProfileById(id: Long, isTeacher: Boolean) = withIO {
        if (isTeacher) {
            cabinetApi.teacherProfile(id).teacherToProfile()
        } else {
            cabinetApi.studentProfile(id).studentToProfile()
        }
    }

    override suspend fun getProjects(userId: Long) = withIO {
        cabinetApi.userStatistic(userId).data.myProjects.map {
            ProjectBasic(
                id = it.id,
                number = it.number,
                name = it.name,
                members = it.students
            )
        }
    }

    override suspend fun getMyProjectsAndApplications() = withIO {
        cabinetApi.myUserStatistic().let {
            val projects = async {
                it.data.projects.data.map {
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
            }

            val applications = async {
                (it.data.applications.data + it.data.approved_applications.data).map {
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
            }

            MyProjectsAndApplications(projects.await(), applications.await())
        }
    }


    override suspend fun applicationWithdraw(applicationId: Long) = applicationConfirm(applicationId, StudentConfirmAction.WITHDRAW)

    override suspend fun applicationApprove(applicationId: Long) = applicationConfirm(applicationId, StudentConfirmAction.APPROVE)

    private suspend fun applicationConfirm(applicationId: Long, action: StudentConfirmAction) = withIO {
        cabinetApi.applicationConfirm(ApplicationConfirmRequest(applicationId, action.status))
    }

    private enum class StudentConfirmAction(val status: Int) {
        WITHDRAW(2),
        APPROVE(1);
    }

    private fun StudentProfileResponse.studentToProfile(): Profile {
        val main = data[0].main
        return Profile(
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

    private fun TeacherProfileResponse.teacherToProfile(): Profile {
        val main = data[0].main
        return Profile(
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
}