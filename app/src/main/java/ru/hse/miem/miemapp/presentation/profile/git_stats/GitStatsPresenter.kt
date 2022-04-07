package ru.hse.miem.miemapp.presentation.profile.git_stats

import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.data.repositories.ProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

class GitStatsPresenter @Inject constructor(
    private val profileRepository: ProfileRepository
): BasePresenter<GitStatsView>() {

    fun onCreate(userId: Long, isTeacher: Boolean) = launch {
        try {
            val email: String?

            val profileId = if (userId == -1L) {
                val profile = profileRepository.getMyProfile()
                email = profile.email

                profile.id
            }
            else {
                email = null
                userId
            }

            println(email)

            profileRepository.getUserGitStatistics(profileId, email, isTeacher).let(viewState::setupGitStats)

        } catch (e: Exception) {
            proceedError(e)
        }
    }
}