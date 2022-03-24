package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val cabinetApi: CabinetApi
) : ISearchRepository {

    override suspend fun getAllProjects() = withIO {
        cabinetApi.allProjects().let {
            it.data.map {
                ProjectInSearch(
                    id = it.id,
                    number = it.number ?: it.id,
                    name = it.nameRus,
                    type = it.typeDesc,
                    state = it.statusDesc,
                    isActive = it.statusId == 2,
                    vacancies = it.vacancies,
                    head = it.head
                )
            }
        }
    }
}