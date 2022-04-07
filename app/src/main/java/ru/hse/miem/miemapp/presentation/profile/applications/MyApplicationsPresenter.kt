package ru.hse.miem.miemapp.presentation.profile.applications

import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

class MyApplicationsPresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
): BasePresenter<MyApplicationsView>() {

    fun onCreate() = launch {
        try {
            profileRepository.getMyProjectsAndApplications().let {
                viewState.setupMyApplications(it.applications)
            }
        }
        catch (e: Exception) {
            proceedError(e)
            viewState.showNoDataFragment()
        }
    }

    fun onApproveApplication(applicationId: Long) = launch {
        try {
            profileRepository.applicationApprove(applicationId)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    fun onWithdrawApplication(applicationId: Long) = launch {
        try {
            profileRepository.applicationWithdraw(applicationId)
        } catch (e: Exception) {
            proceedError(e)
        }
    }
}