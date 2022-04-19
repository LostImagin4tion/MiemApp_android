package ru.hse.miem.miemapp.data.repositories

import kotlinx.coroutines.async
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.VacancyApplyRequest
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import java.lang.Exception
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val cabinetApi: CabinetApi
) : IProjectRepository {

    override suspend fun getProjectById(id: Long) = withIO {
        cabinetApi.projectHeader(id).let {
            val header = it.data
            val body = async { cabinetApi.projectBody(id).data }

            val members = async {
                cabinetApi.projectMembers(id)
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
            }

            val vacancies = async {
                try {
                    cabinetApi.projectVacancies(id).also {
                        /*
                    If error was occurred with response we want to see an exception.
                    However, api returns code 200 even for invalid requests (of course, it's very stupid).
                    So here's a workaround for detecting error response
                     */
                        it.data!!
                    }
                } catch (e: Exception) {
                    cabinetApi.projectVacanciesPublic(id)
                }
                .data
                .filter { !it.booked } // if vacancy is active
                .map {
                    ProjectExtended.Vacancy(
                        id = it.vacancy_id,
                        role = it.role,
                        required = it.disciplines.joinToString(separator = "\n"),
                        recommended = it.additionally.joinToString(separator = "\n"),
                        count = it.count,
                        isApplied = it.applied
                    )
                }
            }

            val gitRepositories = async {
                cabinetApi.gitStatistics(id)
                    .data
                    .getOrNull(0)
                    ?.summary
                    ?.map { ProjectExtended.Link(name = it.project, url = it.link) } ?: emptyList()

            }

            val links = mutableListOf<ProjectExtended.Link>()
            if (header.chat != null) {
                links.add(ProjectExtended.Link("Zulip", header.chat))
            }
            if (header.wiki != null) {
                links.add(ProjectExtended.Link("Wiki", header.wiki))
            }

            val bodyResult = body.await()
            ProjectExtended(
                id = header.id,
                number = header.number.toString().toLongOrNull() ?: header.id,
                type = header.typeLabel,
                source = header.sourceLabel,
                isActive = header.statusValue == 2, // yep, status is just a number
                name = header.nameRus,
                state = header.statusLabel,
                email = header.googleGroup ?: "$id@miem.hse.ru",
                objective = bodyResult.target ?: "",
                annotation = bodyResult.annotation ?: "",
                members = members.await(),
                links =  links + gitRepositories.await(),
                vacancies = vacancies.await(),
                url = CabinetApi.getProjectUrl(id)
            )
            }
    }

    override suspend fun applyForVacancy(vacancyId: Long, aboutMe: String) = withIO {
        cabinetApi.applyForVacancy(VacancyApplyRequest(aboutMe, vacancyId))
    }
}