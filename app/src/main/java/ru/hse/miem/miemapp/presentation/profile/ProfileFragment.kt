package ru.hse.miem.miemapp.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.domain.entities.ProjectBasic
import ru.hse.miem.miemapp.presentation.common.BaseFragment
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileView {

    @Inject
    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @ProvidePresenter
    fun provideProfilePresenter() = profilePresenter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profilePresenter.onCreate()
    }

    override fun setupProfile(profile: Profile) = profile.run {
        userRole.text = getString(if (isTeacher) R.string.teacher_badge else R.string.student_badge)
        userFirstName.text = firstName
        userLastName.text = lastName
        userMail.text = email
        userOccupation.text = occupation
    }

    override fun setupProjects(projects: List<ProjectBasic>) {
        userProjectsLoader.visibility = View.GONE

        if (projects.isNotEmpty()) {
            projectsList.adapter = ProjectsAdapter(projects)
        } else {
            userNoProjectInfo.visibility = View.VISIBLE
        }
    }

    override fun showError() {
        userProjectsLoader.visibility = View.GONE
        userNoProjectInfo.visibility = View.VISIBLE
        Toast.makeText(requireContext(), R.string.common_error_message, Toast.LENGTH_SHORT).show()
    }
}