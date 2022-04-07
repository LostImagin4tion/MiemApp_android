package ru.hse.miem.miemapp.presentation.profile.achievements

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_profile_achievements.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Achievements
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.profile.ProfileFragmentArgs
import javax.inject.Inject

class AchievementsFragment: BaseFragment(R.layout.fragment_profile_achievements), AchievementsView {

    @Inject
    @InjectPresenter
    lateinit var achievementsPresenter: AchievementsPresenter

    @ProvidePresenter
    fun provideAchievementsPresenter() = achievementsPresenter

    private val profileArgs: ProfileFragmentArgs by navArgs()

    private val isMyProfile by lazy { profileArgs.userId < 0 }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAchievements()
    }

    private fun initAchievements() {
        if (isMyProfile) {
            achievementsPresenter.onCreate()
        } else {
            achievementsPresenter.onCreate(profileArgs.userId, profileArgs.isTeacher)
        }
    }

    override fun setupAchievements(achievements: Achievements) {
        achievementsLoader.visibility = View.GONE

        trackingAchievementsList.adapter = TrackerAchievementsAdapter(
            achievements.tracker
        )
        gitlabAchievementsList.adapter = GitlabAchievementsAdapter(
            achievements.gitlab
        )

        trackingTitle.visibility = View.VISIBLE
        gitlabTitle.visibility = View.VISIBLE

        if (achievements.tracker.isNotEmpty()) {
            trackingNoInfo.visibility = View.GONE
            trackingSection.visibility = View.VISIBLE
        }
        else {
            trackingNoInfo.visibility = View.VISIBLE
        }

        if (achievements.gitlab.isNotEmpty()) {
            gitlabNoInfo.visibility = View.GONE
            gitlabSection.visibility = View.VISIBLE
        }
        else {
            gitlabNoInfo.visibility = View.VISIBLE
        }
    }

    override fun showNoDataTrackingSection() {
        trackingSection.visibility = View.GONE
        achievementsLoader.visibility = View.GONE
        trackingNoInfo.visibility = View.VISIBLE
    }

    override fun showNoDataGitlabSection() {
        gitlabSection.visibility = View.GONE
        achievementsLoader.visibility = View.GONE
        gitlabNoInfo.visibility = View.VISIBLE
    }
}