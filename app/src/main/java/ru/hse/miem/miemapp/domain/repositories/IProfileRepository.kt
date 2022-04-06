package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.*

interface IProfileRepository {
    suspend fun getMyProfile(): Profile
    suspend fun getProfileById(id: Long, isTeacher: Boolean = false): Profile
    suspend fun getProjects(userId: Long): List<ProjectBasic>
    suspend fun getMyProjectsAndApplications(): MyProjectsAndApplications
    suspend fun getAchievementsWithProgress(userId: Long, email: String?): Achievements
    suspend fun getUserGitStatistics(userId: Long, email: String?, isTeacher: Boolean): List<UserGitStatistics>
    suspend fun getMyUserGitStatistics(): List<UserGitStatistics>
    suspend fun applicationWithdraw(applicationId: Long)
    suspend fun applicationApprove(applicationId: Long)
}