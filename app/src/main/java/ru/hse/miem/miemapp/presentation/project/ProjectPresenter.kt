package ru.hse.miem.miemapp.presentation.project

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class ProjectPresenter @Inject constructor(
    private val projectRepository: IProjectRepository
) : BasePresenter<ProjectView>() {

    fun onCreate(projectId: Long) = launch {
        try {
            projectRepository.getProjectById(projectId).let(viewState::setupProject)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    fun onSubmitVacancyApplication(vacancyId: Long, aboutMe: String, onComplete: () -> Unit, onError: () -> Unit) = launch {
        try {
            projectRepository.applyForVacancy(vacancyId, aboutMe)
            onComplete()
        } catch (e: Exception) {
            proceedError(e)
            onError()
        }
    }

}