package ru.hse.miem.miemapp.data.repositories

import io.reactivex.Single
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.StudentProfileResponse
import ru.hse.miem.miemapp.data.api.TeacherProfileResponse
import ru.hse.miem.miemapp.domain.entities.MyProjectBasic
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

    override fun getMyProjects() = cabinetApi.myUserStatistic()
        .map {
            it.data.projects.data.map {
                MyProjectBasic(
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

    private fun Single<StudentProfileResponse>.studentToProfile() = map {
        val main = it.data[0].main
        Profile(
            id = main.studentId,
            isTeacher = false,
            firstName = main.name.split(" ")[1],
            lastName = main.name.split(" ")[0],
            email = main.email,
            occupation = main.group,
            avatarUrl = CabinetApi.getAvatarUrl(main.studentId)
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
            avatarUrl = CabinetApi.getAvatarUrl(main.teacherId)
        )
    }
}