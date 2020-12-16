package ru.hse.miem.miemcam.presentation.cameras

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.fragment_cameras_list.*
import ru.hse.miem.miemcam.presentation.main.CamerasActivity
import javax.inject.Inject

// FIXME DO NOT PASS ARGUMENTS TO FRAGMENT VIA CONSTRUCTOR
class CamerasListFragment(
  private val setLoadingAnimationVisibility: (Boolean) -> Unit,
  private val setToolbarName: (String) -> Unit
) : MvpAppCompatFragment(), CamerasListView {

  @Inject
  @InjectPresenter
  lateinit var camerasListPresenter: CamerasListPresenter

  @ProvidePresenter
  fun provideCamerasListPresenter() = camerasListPresenter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_cameras_list, container, false)
  }

  override fun onAttach(context: Context) {
    (activity as CamerasActivity).camerasComponent.inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    camerasListPresenter.onViewCreated()
  }

  override fun setToolbarLabel(text: String) {
    setToolbarName(text)
  }

  override fun setLoadingVisibility(isVisible: Boolean) {
    setLoadingAnimationVisibility(isVisible)
  }

  override fun setUpCamerasListView(camerasAdapter: CamerasAdapter) {
    val viewManager = LinearLayoutManager(this.context)
    camerasListView.layoutManager = viewManager
    camerasListView.adapter = camerasAdapter
  }

  override fun updateCamerasListView() {
    activity?.runOnUiThread {
      camerasListView.adapter?.notifyDataSetChanged()
    }
  }

  override fun stopLoadAnimation() {
    activity?.runOnUiThread {
      loadingProgressList.visibility = View.INVISIBLE
    }
  }

  override fun showError() {
    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
  }

  fun reload() {
    camerasListPresenter.reload()
  }
}
