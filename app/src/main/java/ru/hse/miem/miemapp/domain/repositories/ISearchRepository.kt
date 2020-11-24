package ru.hse.miem.miemapp.domain.repositories

import io.reactivex.Single
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch

interface ISearchRepository {
    fun getAllProjects(): Single<List<ProjectInSearch>>
}