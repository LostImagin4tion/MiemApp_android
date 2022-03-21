package ru.hse.miem.miemapp.presentation.profile

import androidx.navigation.NavArgs
import moxy.InjectViewState
import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import ru.hse.miem.miemapp.presentation.schedule.ScheduleFragmentArgs
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
    private val session: Session
) : BasePresenter<ProfileView>() {

    fun onCreate(userId: Long? = null, isTeacher: Boolean? = null) = launch {
        try {
            val profile = if (userId != null && isTeacher != null) {
                profileRepository.getProfileById(userId, isTeacher)
            } else {
                profileRepository.getMyProfile()
            }

            viewState.setupProfile(profile)

            // because idk how to show my profile for teacher
            // FIXME maybe i'll investigate
            if (userId == null && session.isStudent) {
                loadMyProjects()
            } else {
                loadProjects(profile.id)
            }
        } catch (e: Exception) {
            proceedError(e)
            viewState.showUnauthorizedProfile()
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
                viewState.setupMyApplications(it.applications)
            }
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    fun onApproveApplication(applicationId: Long) = launch {
        try {
            profileRepository.applicationApprove(applicationId)
        } catch (e: Exception) {
            proceedError(e)
        } finally {
            loadMyProjects()
        }
    }

    fun onWithdrawApplication(applicationId: Long) = launch {
        try {
            profileRepository.applicationWithdraw(applicationId)
        } catch (e: Exception) {
            proceedError(e)
        } finally {
            loadMyProjects()
        }
    }
}