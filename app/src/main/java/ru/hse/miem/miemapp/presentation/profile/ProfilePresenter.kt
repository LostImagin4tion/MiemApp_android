package ru.hse.miem.miemapp.presentation.profile

import moxy.InjectViewState
import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
) : BasePresenter<ProfileView>() {

    fun onCreate(userId: Long? = null, isTeacher: Boolean? = null) = launch {
        try {
            val profile = if (userId != null && isTeacher != null) {
                profileRepository.getProfileById(userId, isTeacher)
            } else {
                profileRepository.getMyProfile()
            }

            viewState.setupProfile(profile)

        } catch (e: Exception) {
            proceedError(e)
            viewState.showUnauthorizedProfile()
        }
    }
}