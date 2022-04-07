package ru.hse.miem.miemapp.presentation.profile.achievements

import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

class AchievementsPresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
): BasePresenter<AchievementsView>() {

    fun onCreate(userId: Long? = null, isTeacher: Boolean? = null) = launch {
        try {
            val email: String?

            val profileId = if (userId == null) {
                val profile = profileRepository.getMyProfile()
                email = profile.email

                profile.id
            }
            else {
                email = null
                userId
            }

            profileRepository.getAchievementsWithProgress(profileId, email).let {
                viewState.setupAchievements(it)
            }
        } catch (e: Exception) {
            proceedError(e)
            viewState.showNoDataTrackingSection()
            viewState.showNoDataGitlabSection()
        }
    }
}