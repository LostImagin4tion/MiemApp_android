package ru.hse.miem.miemapp.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.hse.miem.miemapp.BuildConfig
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.presentation.main.MainActivity
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var signInClient: GoogleSignInClient

    @Inject
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonLogout.setOnClickListener { logout() }
        buttonSubmitReport.setOnClickListener {
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