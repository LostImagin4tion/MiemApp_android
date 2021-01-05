package ru.hse.miem.miemapp.presentation.project

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import javax.inject.Inject


class ProjectFragment : BaseFragment(R.layout.fragment_project), ProjectView {

    @Inject
    @InjectPresenter
    lateinit var projectPresenter: ProjectPresenter

    @ProvidePresenter
    fun provideProjectPresenter() = projectPresenter

    private val args: ProjectFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectPresenter.onCreate(args.projectId)
        projectEmail.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", projectEmail.text.toString(), null)),
                    getString(R.string.send_mail_dialog_title)
                )
            )
        }
    }

    override fun setupProject(project: ProjectExtended) = project.run {
        projectType.text = getString(R.string.project_type).format(number, type, source)
        projectName.text = name
        projectState.text = state
        projectState.setBackgroundResource(if (isActive) R.drawable.project_badge_active_bg else R.drawable.project_badge_inactive_bg)
        projectEmail.text = email
        projectObjective.text = objective
        projectAnnotation.text = annotation
        membersList.adapter = MembersAdapter(members) { id, isTeacher ->
            val action = ProjectFragmentDirections.actionFragmentProjectToFragmentProfile(id, isTeacher)
            findNavController().navigate(action)
        }

        if (links.isNotEmpty()) {
            linksList.adapter = LinksAdapter(links)
            linksNoData.visibility = View.GONE
            linksList.visibility = View.VISIBLE
        }

        if (vacancies.isNotEmpty()) {
            vacanciesList.adapter = VacanciesAdapter(vacancies)
            vacanciesNoData.visibility = View.GONE
            vacanciesList.visibility = View.VISIBLE
        }

        projectOpenInBrowserButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        projectLoader.visibility = View.GONE
        projectContent.visibility = View.VISIBLE
    }
}