package ru.hse.miem.miemapp.presentation.profile.git_stats

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_profile_git_stats.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.UserGitStatistics
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.profile.ProfileFragmentArgs
import ru.hse.miem.miemapp.presentation.schedule.ScheduleAdapter
import javax.inject.Inject

class GitStatsFragment: BaseFragment(R.layout.fragment_profile_git_stats), GitStatsView {

    @Inject
    @InjectPresenter
    lateinit var gitStatsPresenter: GitStatsPresenter

    @ProvidePresenter
    fun provideGitStatsPresenter() = gitStatsPresenter

    private lateinit var repos: List<UserGitStatistics>

    private val profileArgs: GitStatsFragmentArgs by navArgs()

    private val gitStatsAdapter = GitStatsAdapter()

    private val filters = GitStatsFilters()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        languagesList.adapter = gitStatsAdapter

        gitProjectNameSelector.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                restoreFilters()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterResults()
            }
        }

        gitStatsPresenter.onCreate(profileArgs.userId, profileArgs.isTeacher)
    }

    private fun filterResults() {
        gitStatsLoader.visibility = View.VISIBLE

        filters.project = gitProjectNameSelector.selectedItemPosition
        filters.projectName = gitProjectNameSelector.selectedItem as String

        val displayedRepo = repos.filter { it.name == filters.projectName }[0]

        updateGitStats(displayedRepo)

        gitStatsLoader.visibility = View.GONE
    }

    private fun restoreFilters() {
        gitProjectNameSelector.setSelection(filters.project)
    }

    private fun updateGitStats(gitStats: UserGitStatistics) {
        commitsCount.text = gitStats.commitCount.toString()
        stringsCount.text = gitStats.stringsCount.toString()

        if (gitStats.repoId == 0L) {
            reposSection.visibility = View.VISIBLE
            reposCount.text = (repos.size - 1).toString()
        }
        else {
            reposSection.visibility = View.GONE
        }

        gitStatsAdapter.update(gitStats.usedLanguages)
    }

    override fun setupGitStats(gitStats: List<UserGitStatistics>) {
        repos = gitStats
        gitStatsAdapter.update(gitStats[0].usedLanguages)

        gitProjectNameSelector.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_simple,
            gitStats.map { it.name }.toSet().toList()
        )
        restoreFilters()

        commitsCount.text = gitStats[0].commitCount.toString()
        stringsCount.text = gitStats[0].stringsCount.toString()
        reposCount.text = (repos.size - 1).toString()

        gitStatsLoader.visibility = View.GONE

        commitsSection.visibility = View.VISIBLE
        stringsSection.visibility = View.VISIBLE
        reposSection.visibility = View.VISIBLE
        languagesSection.visibility = View.VISIBLE
        languagesList.visibility = View.VISIBLE
    }
}