package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val cabinetApi: CabinetApi
) : IProjectRepository {

    override fun getProjectById(id: Long) = cabinetApi.projectHeader(id)
        .map {
            val header = it.data
            val body = cabinetApi.projectBody(id).blockingGet().data
            val members = cabinetApi.projectMembers(id)
                .blockingGet()
                .data
                .let { it.leaders + it.activeMembers }
                .map {
                    ProjectExtended.Member(
                        id = it.id,
                        firstName = it.first_name ?: it.name,
                        lastName = it.last_name ?: "",
                        isTeacher = it.dep?.isNotEmpty() ?: false,
                        role = it.role,
                        avatarUrl = CabinetApi.getAvatarUrl(it.id)
                    )
                }
            val vacancies = cabinetApi.projectVacancies(id)
                .blockingGet()
                .data
                .filter { !it.booked } // if vacancy is active
                .map{
                    ProjectExtended.Vacancy(
                        id = it.vacancy_id,
                        role = it.role,
                        required = it.disciplines.joinToString(separator = "\n"),
                        recommended = it.additionally.joinToString(separator = "\n"),
                        count = it.count,
                        isApplied = it.applied
                    )
                }
            val gitRepositories = cabinetApi.gitStatistics(id)
                .blockingGet()
                .data.getOrNull(0)
                ?.summary
                ?.map { ProjectExtended.Link(name = it.project, url = it.link) } ?: emptyList()

            ProjectExtended(
                id = header.id,
                number = header.number.toString().toLongOrNull() ?: header.id,
                type = header.typeLabel,
                source = header.sourceLabel,
                isActive = header.statusValue == 2, // yep, status is just a number
                name = header.nameRus,
                state = header.statusLabel,
                email = header.googleGroup ?: "$id@miem.hse.ru",
                objective = body.target ?: "",
                annotation = body.annotation ?: "",
                members = members,
                links = listOf(ProjectExtended.Link("Trello", header.trello)) + gitRepositories,
                vacancies = vacancies,
                url = CabinetApi.getProjectUrl(id)
            )
        }

    // https://cabinet.miem.hse.ru/api/student/application/add to apply for vacancy
    // {
    //   about_me: "text",
    //   vacancy_id: id
    // }

}