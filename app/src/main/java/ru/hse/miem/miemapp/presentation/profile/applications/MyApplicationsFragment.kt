package ru.hse.miem.miemapp.presentation.profile.applications

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile_applications.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.MyProjectsAndApplications
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.profile.projects.ProjectsFragmentDirections
import javax.inject.Inject

class MyApplicationsFragment: BaseFragment(R.layout.fragment_profile_applications), MyApplicationsView {

    @Inject
    @InjectPresenter
    lateinit var applicationsPresenter: MyApplicationsPresenter

    @ProvidePresenter
    fun provideApplicationsPresenter() = applicationsPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        applicationsPresenter.onCreate()
    }

    override fun setupMyApplications(applications: List<MyProjectsAndApplications.MyApplication>) {
        applicationsLoader.visibility = View.GONE

        applicationsList.adapter = MyApplicationsAdapter(
            applications,
            navigateToProject = {
                val action = ProjectsFragmentDirections.actionFragmentProfileProjectsToFragmentProject(it)
                findNavController().navigate(action)
            },
            withdrawApplication = applicationsPresenter::onWithdrawApplication,
            approveApplication = applicationsPresenter::onApproveApplication
        )
        if (applications.isNotEmpty()) {
            userNoApplicationsInfo.visibility = View.GONE
        } else {
            userNoApplicationsInfo.visibility = View.VISIBLE
        }
    }

    override fun showNoDataFragment() {
        userApplications.visibility = View.GONE
        applicationsLoader.visibility = View.GONE
        userNoApplicationsInfo.visibility = View.VISIBLE
    }
}