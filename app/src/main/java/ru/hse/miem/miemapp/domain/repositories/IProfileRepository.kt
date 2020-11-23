package ru.hse.miem.miemapp.domain.repositories

import io.reactivex.Single
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.domain.entities.ProjectBasic

interface IProfileRepository {
    fun getMyProfile(): Single<Profile>
    fun getProfileById(id: Long, isTeacher: Boolean = false): Single<Profile>
    fun getProjects(userId: Long): Single<List<ProjectBasic>>
}