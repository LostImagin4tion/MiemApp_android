package ru.hse.miem.miemapp.data.repositories

import kotlinx.coroutines.async
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.data.api.ApplicationConfirmRequest
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.StudentProfileResponse
import ru.hse.miem.miemapp.data.api.TeacherProfileResponse
import ru.hse.miem.miemapp.domain.entities.*
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
            try {
                cabinetApi.teacherProfile(id).teacherToProfile()
            } catch (e: Exception) {
                cabinetApi.studentProfile(id).studentToProfile()
            }
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
                it.data.projects.map {
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
                (it.data.applications + it.data.approved_applications).map {
                    MyProjectsAndApplications.MyApplication(
                        id = it.applId,
                        projectId = it.id,
                        projectNumber = it.nameRus.split(" ")[0].toLong(),
                        projectName = it.nameRus,
                        projectType = it.type,
                        role = it.role,
                        head = it.head,
                        status = MyProjectsAndApplications.MyApplication.Status.valueOf(it.status),
                        studentComment = it.studentComment,
                        headComment = it.leaderComment
                    )
                }
            }

            MyProjectsAndApplications(projects.await(), applications.await())
        }
    }

    override suspend fun getAchievementsWithProgress(userId: Long, email: String?) = withIO {

        val profileId = if (email != null) {
            cabinetApi.userInfoByEmail(email).data.userId.toLong()
        } else {
            userId
        }

        cabinetApi.achievementsWithProgress(profileId).let {

            val tracker: MutableList<Achievements.Tracker> = mutableListOf()
            val gitlab: MutableList<Achievements.Gitlab> = mutableListOf()

            it.data.map {
                if (it.category_id == 1) {
                    tracker.add(Achievements.Tracker(
                        id = it.id,
                        name = it.name,
                        categoryId = it.category_id,
                        awardCondition = it.award_condition_description,
                        image = it.image,
                        progress = it.progress.toInt()
                    ))
                }
                else {
                    gitlab.add(Achievements.Gitlab(
                        id = it.id,
                        name = it.name,
                        categoryId = it.category_id,
                        awardCondition = it.award_condition_description,
                        image = it.image,
                        progress = it.progress.toInt()
                    ))
                }
            }

            Achievements(tracker, gitlab)
        }
    }

    override suspend fun getUserGitStatistics(userId: Long, email: String?, isTeacher: Boolean) = withIO {

        val profileId = if(email != null) {
            cabinetApi.userInfoByEmail(email).data.userId.toLong()
        } else {
            userId
        }

        if (isTeacher) {
            val stats = cabinetApi.teacherGitStatistics(profileId).let {
                it.data.map {
                    UserGitStatistics(
                        repoId = it.id,
                        name = it.name,
                        commitCount = it.commitCount,
                        stringsCount = it.strings,
                        usedLanguages = it.languages
                    )
                }
            }
            stats.ifEmpty {
                cabinetApi.studentGitStatistics(profileId).let {
                    it.data.map {
                        UserGitStatistics(
                            repoId = it.id,
                            name = it.name,
                            commitCount = it.commitCount,
                            stringsCount = it.strings,
                            usedLanguages = it.languages
                        )
                    }
                }
            }
        }
        else {
            cabinetApi.studentGitStatistics(profileId).let {
                it.data.map {
                    UserGitStatistics(
                        repoId = it.id,
                        name = it.name,
                        commitCount = it.commitCount,
                        stringsCount = it.strings,
                        usedLanguages = it.languages
                    )
                }
            }
        }
    }

    override suspend fun getMyUserGitStatistics() = withIO {
        cabinetApi.myUserGitStatistics().let {
            it.data.map {
                UserGitStatistics(
                    repoId = it.id,
                    name = it.name,
                    commitCount = it.commitCount,
                    stringsCount = it.strings,
                    usedLanguages = it.languages
                )
            }
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