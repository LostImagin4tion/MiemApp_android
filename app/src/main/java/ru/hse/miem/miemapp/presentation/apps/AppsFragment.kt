package ru.hse.miem.miemapp.presentation.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.fragment_apps.*
import ru.hse.miem.miemapp.BuildConfig
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.presentation.sandbox.SandboxFragment
import ru.hse.miem.miemapp.presentation.sandbox.SandboxFragmentDirections

class AppsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appsList.layoutManager = FlexboxLayoutManager(requireContext()).also {
            it.flexDirection = FlexDirection.ROW
        }

        appsList.adapter = AppsAdapter(
            listOf(
                AppItem(
                    icon = ContextCompat.getDrawable(
                        requireContext(),
                        resources.getIdentifier(
                            "app_icon_sandbox",
                            "drawable",
                            BuildConfig.APPLICATION_ID
                        )
                    )!!,
                    name = getString(R.string.title_sandbox),
                    isExternal = false,
                    navigateAction = AppsFragmentDirections.actionFragmentAppsToFragmentSandbox(),
                ),
                AppItem(
                    icon = ContextCompat.getDrawable(
                        requireContext(),
                        resources.getIdentifier(
                            "app_icon_indoor",
                            "drawable",
                            BuildConfig.APPLICATION_ID
                        )
                    )!!,
                    name = getString(R.string.title_indoor),
                    activityClassName = "ru.miem.indoor_dynamicfeature.IndoorActivity",
                    isExternal = true
                )
            )
        )
    }
}