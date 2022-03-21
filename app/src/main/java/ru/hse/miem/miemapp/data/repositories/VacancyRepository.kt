package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.domain.repositories.IVacancyRepository
import javax.inject.Inject

class VacancyRepository @Inject constructor(
    private val cabinetApi: CabinetApi
): IVacancyRepository {
    override suspend fun getAllVacancies() = withIO {
        cabinetApi.allVacancies().let{
                it.data.map {
                    Vacancies(
                        vacancyId = it.vacancy_id?: 0,
                        projectId = it.project_id?: 0,
                        projectNameRus = it.project_name_rus?: "",
                        vacancyRole = it.vacancy_role?: "",
                        vacancyDisciplines = it.vacancy_disciplines?: emptyList(),
                        vacancyAdditionally = it.vacancy_disciplines?: emptyList()
                    )
                }
            }
    }
}