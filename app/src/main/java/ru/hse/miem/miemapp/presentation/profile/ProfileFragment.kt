package ru.hse.miem.miemapp.presentation.profile

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.hse.miem.miemapp.BuildConfig
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.presentation.OnBackPressListener
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.main.MainActivity
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileView, OnBackPressListener {

    @Inject
    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @ProvidePresenter
    fun provideProfilePresenter() = profilePresenter

    @Inject
    lateinit var signInClient: GoogleSignInClient

    @Inject
    lateinit var session: Session

    private lateinit var settingsButtonBehavior: BottomSheetBehavior<View>

    private val profileArgs: ProfileFragmentArgs by navArgs()

    private val isMyProfile by lazy { profileArgs.userId < 0 }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProfile()

        userEmail.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", userEmail.text.toString(), null)),
                    getString(R.string.send_mail_dialog_title)
                )
            )
        }
        profileSwipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        profileSwipeRefreshLayout.setOnRefreshListener(::initProfile)

        settingsButtonBehavior = BottomSheetBehavior.from(settingsLayout)
        settingsButtonBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        settingsButtonBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    profileSwipeRefreshLayout.foreground = ColorDrawable(
                        resources.getColor(R.color.transparent)
                    )
                }
            }
        })

        projectsButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionFragmentProfileToFragmentProfileProjects())
        }

        applicationButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionFragmentProfileToFragmentProfileApplications())
        }

        achievementsButton.setOnClickListener {
            findNavController()
                .navigate(ProfileFragmentDirections.actionFragmentProfileToFragmentProfileAchievements(
                    userId = profileArgs.userId,
                    isTeacher = profileArgs.isTeacher
                ))
        }

        gitButton.setOnClickListener {
            findNavController()
                .navigate(ProfileFragmentDirections.actionFragmentProfileToFragmentProfileGitStats(
                    userId = profileArgs.userId,
                    isTeacher = profileArgs.isTeacher
            ))
        }

        settingsButton.setOnClickListener {
            profileSwipeRefreshLayout.foreground = ColorDrawable(
                resources.getColor(R.color.semi_transparent)
            )
            settingsButtonBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        hideSettingsButton.setOnClickListener {
            profileSwipeRefreshLayout.foreground = ColorDrawable(
                resources.getColor(R.color.transparent)
            )
            settingsButtonBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        reportButton.setOnClickListener { submitReport() }

        exitButton.setOnClickListener { logout() }
    }

    override fun onBackPressed(): Boolean {
        if (settingsButtonBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            profileSwipeRefreshLayout.foreground = ColorDrawable(
                resources.getColor(R.color.transparent)
            )
            settingsButtonBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false
    }

    private fun initProfile() {
        if (isMyProfile) {
            profilePresenter.onCreate()
        } else {
            profileAppSection.visibility = View.INVISIBLE
            applicationButton.visibility = View.GONE
            profilePresenter.onCreate(profileArgs.userId, profileArgs.isTeacher)
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

        if (profile.chatUrl != null) {
            goToChatButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(chatUrl)))
            }
        } else {
            goToChatButton.visibility = View.GONE
        }

        if (isTeacher) {
            applicationButton.visibility = View.GONE
        }

        profileLoader.visibility = View.GONE
        profileContent.visibility = View.VISIBLE
        profileInfo.visibility = View.VISIBLE

        profileSwipeRefreshLayout.isRefreshing = false
    }

    override fun showUnauthorizedProfile() {
        profileLoader.visibility = View.GONE
        profileContent.visibility = View.VISIBLE
        profileInfo.visibility = View.GONE
    }

    private fun submitReport() {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).also {
                    it.putExtra(Intent.EXTRA_EMAIL, arrayOf(MiemApplication.DEVELOPER_MAIL))
                    it.putExtra(Intent.EXTRA_STREAM, (requireActivity().application as MiemApplication).currentLogFileUri)
                    it.putExtra(Intent.EXTRA_SUBJECT, "Report. Version ${BuildConfig.VERSION_NAME}")
                    it.putExtra(Intent.EXTRA_TEXT, "Android: ${Build.VERSION.RELEASE}\nDevice: ${Build.MODEL}\nDescribe in details your problem:")
                },
                getString(R.string.send_mail_dialog_title)
            )
        )
    }

    private fun logout() {
        signInClient.signOut().addOnCompleteListener {
            session.reset()
            (activity as MainActivity).apply {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }
}