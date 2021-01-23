package ru.hse.miem.miemapp.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_settings.*
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