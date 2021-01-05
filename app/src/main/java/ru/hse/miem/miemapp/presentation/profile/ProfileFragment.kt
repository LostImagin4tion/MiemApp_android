package ru.hse.miem.miemapp.presentation.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.MyProjectBasic
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.domain.entities.ProjectBasic
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileView {

    @Inject
    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @ProvidePresenter
    fun provideProfilePresenter() = profilePresenter

    private val args: ProfileFragmentArgs by navArgs()
    private val isMyProfile by lazy { args.userId < 0 }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isMyProfile) {
            profilePresenter.onCreate()
        } else {
            profilePresenter.onCreate(args.userId, args.isTeacher)
        }
        userEmail.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", userEmail.text.toString(), null)),
                    getString(R.string.send_mail_dialog_title)
                )
            )
        }
    }

    override fun setupProfile(profile: Profile) = profile.run {
        userRole.text = getString(if (isTeacher) R.string.teacher_badge else R.string.student_badge)
        userFirstName.text = firstName
        userLastName.text = lastName
        Glide.with(requireContext())
            .load(avatarUrl)
            .apply(RequestOptions().circleCrop())
            .into(userAvatar)
        userEmail.text = email
        userOccupation.text = occupation

        profileLoader.visibility = View.GONE
        profileContent.visibility = View.VISIBLE
    }

    override fun setupProjects(projects: List<ProjectBasic>) {
        userProjectsLoader.visibility = View.GONE

        if (projects.isNotEmpty()) {
            projectsList.adapter = ProjectsAdapter(projects) {
                val action = ProfileFragmentDirections.actionFragmentProfileToFragmentProject(it)
                findNavController().navigate(action)
            }
        } else {
            userNoProjectInfo.visibility = View.VISIBLE
        }
    }

    override fun setupMyProjects(projects: List<MyProjectBasic>) {
        userProjectsLoader.visibility = View.GONE

        if (projects.isNotEmpty()) {
            projectsList.adapter = MyProjectsAdapter(projects) {
                val action = ProfileFragmentDirections.actionFragmentProfileToFragmentProject(it)
                findNavController().navigate(action)
            }
        } else {
            userNoProjectInfo.visibility = View.VISIBLE
        }
    }
}