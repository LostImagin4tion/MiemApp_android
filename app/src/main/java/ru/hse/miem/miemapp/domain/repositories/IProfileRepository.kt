package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.MyProjectsAndApplications
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.domain.entities.ProjectBasic

interface IProfileRepository {
    suspend fun getMyProfile(): Profile
    suspend fun getProfileById(id: Long, isTeacher: Boolean = false): Profile
    suspend fun getProjects(userId: Long): List<ProjectBasic>
    suspend fun getMyProjectsAndApplications(): MyProjectsAndApplications
    suspend fun applicationWithdraw(applicationId: Long)
    suspend fun applicationApprove(applicationId: Long)
}