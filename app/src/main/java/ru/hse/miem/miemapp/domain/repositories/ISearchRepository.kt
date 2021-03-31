package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.ProjectInSearch

interface ISearchRepository {
    suspend fun getAllProjects(): List<ProjectInSearch>
}