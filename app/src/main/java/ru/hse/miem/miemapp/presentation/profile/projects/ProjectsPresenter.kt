package ru.hse.miem.miemapp.presentation.profile.projects

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class ProjectsPresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
    private val session: Session
): BasePresenter<ProjectsView>() {

    fun onCreate(userId: Long? = null) = launch {
        try {

            // because idk how to show my profile for teacher
            // FIXME maybe i'll investigate
            if (userId == null && session.isStudent) {
                loadMyProjects()
            } else {
                loadProjects(userId!!)
            }
        } catch (e: Exception) {
            proceedError(e)
            viewState.showNoDataFragment()
        }
    }

    private fun loadProjects(userId: Long) = launch {
        try {
            profileRepository.getProjects(userId).let(viewState::setupProjects)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    private fun loadMyProjects() = launch {
        try {
            profileRepository.getMyProjectsAndApplications().let {
                viewState.setupMyProjects(it.projects)
            }
        } catch (e: Exception) {
            proceedError(e)
        }
    }
}