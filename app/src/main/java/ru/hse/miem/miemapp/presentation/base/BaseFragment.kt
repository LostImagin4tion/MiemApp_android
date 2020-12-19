package ru.hse.miem.miemapp.presentation.base

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import moxy.MvpAppCompatFragment
import com.google.android.material.snackbar.Snackbar
import ru.hse.miem.miemapp.R

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : MvpAppCompatFragment(), BaseView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun showError() {
        Snackbar.make(requireView(), R.string.common_error_message, Snackbar.LENGTH_SHORT).let {
            // yep, its deprecated, but our min api is 22, so its nothing to do with it
            it.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            it.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).let {
                it.setTextColor(resources.getColor(android.R.color.black))
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }
            it.show()

        }
    }
}