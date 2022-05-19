package ru.hse.miem.miemapp.presentation.profile.projects

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_profile_projects.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.MyProjectsAndApplications
import ru.hse.miem.miemapp.domain.entities.ProjectBasic
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.profile.ProfileFragmentArgs
import javax.inject.Inject

class ProjectsFragment : BaseFragment(R.layout.fragment_profile_projects), ProjectsView {

    @Inject
    @InjectPresenter
    lateinit var projectsPresenter: ProjectsPresenter

    @ProvidePresenter
    fun provideProjectsPresenter() = projectsPresenter

    private val profileArgs: ProfileFragmentArgs by navArgs()

    private val isMyProfile by lazy { profileArgs.userId < 0 }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (isMyProfile) {
            profileProjectsSwipeRefreshLayout.setOnRefreshListener { projectsPresenter.onCreate() }

            projectsPresenter.onCreate()
        } else {
            profileProjectsSwipeRefreshLayout.setOnRefreshListener {
                projectsPresenter.onCreate(profileArgs.userId)
            }

            projectsPresenter.onCreate(profileArgs.userId)
        }
    }

    override fun setupProjects(projects: List<ProjectBasic>) {
        projectsLoader.visibility = View.GONE

        if (projects.isNotEmpty()) {
            projectsList.adapter = ProjectsAdapter(projects) {
                val action = ProjectsFragmentDirections.actionFragmentProfileProjectsToFragmentProject(it)
                findNavController().navigate(action)
            }
            userNoProjectInfo.visibility = View.GONE
        } else {
            userNoProjectInfo.visibility = View.VISIBLE
        }
        profileProjectsSwipeRefreshLayout.isRefreshing = false
    }

    override fun setupMyProjects(projects: List<MyProjectsAndApplications.MyProjectBasic>) {
        projectsLoader.visibility = View.GONE

        if (projects.isNotEmpty()) {
            projectsList.adapter = MyProjectsAdapter(projects) {
                val action = ProjectsFragmentDirections.actionFragmentProfileProjectsToFragmentProject(it)
                findNavController().navigate(action)
            }
            userNoProjectInfo.visibility = View.GONE
        } else {
            userNoProjectInfo.visibility = View.VISIBLE
        }
        profileProjectsSwipeRefreshLayout.isRefreshing = false
    }

    override fun showNoDataFragment() {
        projectsList.visibility = View.GONE
        projectsLoader.visibility = View.GONE
        userNoProjectInfo.visibility = View.VISIBLE
    }
}