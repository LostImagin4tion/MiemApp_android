package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
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
                        vacancy_id = it.vacancy_id?: 0,
                        project_id = it.project_id?: 0,
                        project_name_rus = it.project_name_rus?: "",
                        vacancy_role = it.vacancy_role?: "",
                        vacancy_disciplines = it.vacancy_disciplines?: emptyList(),
                        vacancy_additionally = it.vacancy_disciplines?: emptyList()
                    )
                }
            }
    }
}