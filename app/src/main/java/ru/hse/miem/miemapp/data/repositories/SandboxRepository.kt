package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.entities.ProjectInSandbox
import ru.hse.miem.miemapp.domain.repositories.ISandboxRepository
import javax.inject.Inject

class SandboxRepository @Inject constructor(
    val cabinetApi: CabinetApi
): ISandboxRepository {

    override suspend fun getSandboxProjects() = withIO {
        cabinetApi.projectSandbox().let {
            it.data.map {
                ProjectInSandbox(
                    id = it.id,
                    stateId = it.statusId,
                    state = it.statusDesc,
                    name = it.nameRus,
                    head = it.head,
                    type = it.typeDesc,
                    vacancies = it.vacancies
                )
            }
        }
    }
}