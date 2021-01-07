package ru.hse.miem.miemapp.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import ru.hse.miem.miemapp.domain.entities.ProjectExtended

interface IProjectRepository {
    fun getProjectById(id: Long): Single<ProjectExtended>
    fun applyForVacancy(vacancyId: Long, aboutMe: String): Completable
}