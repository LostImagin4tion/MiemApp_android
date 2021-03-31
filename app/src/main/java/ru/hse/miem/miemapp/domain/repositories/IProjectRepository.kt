package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.ProjectExtended

interface IProjectRepository {
    suspend fun getProjectById(id: Long): ProjectExtended
    suspend fun applyForVacancy(vacancyId: Long, aboutMe: String)
}