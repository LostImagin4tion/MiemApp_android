package ru.hse.miem.miemcam.presentation.main

import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.domain.repositories.IAuthRepository
import ru.hse.miem.miemcam.presentation.cameras.CamerasListFragment
import ru.hse.miem.miemcam.presentation.control.ControlPanelFragment
import ru.hse.miem.miemcam.presentation.record.RecordFragment
import ru.hse.miem.miemcam.presentation.util.FragmentType
import ru.hse.miem.miemcam.presentation.util.MenuItem
import ru.hse.miem.miemcam.presentation.vmix.VmixControlFragment
import javax.inject.Inject

interface CamerasView : MvpView {
  @AddToEndSingle fun setUpViews()
  @AddToEndSingle fun setUpMenu(menuItemAdapter: MenuItemAdapter)
  @AddToEndSingle fun setFragment(f: Fragment)
  @AddToEndSingle fun setBackgroundFragment(f: Fragment)
  @AddToEndSingle fun openCameraPicker()
  @AddToEndSingle fun closeCameraPicker()
  @AddToEndSingle fun setActionBarLabel(text: String)
  @AddToEndSingle fun setArrowVisibility(isVisible: Boolean)
  @AddToEndSingle fun setLoadingVisibility(isVisible: Boolean)
  @AddToEndSingle fun setMoreVisibility(isVisible: Boolean)
  @AddToEndSingle fun updateMenuItems()
  @AddToEndSingle fun selectMoreNavigation()
  @OneExecution fun showError()
}

@InjectViewState
class CamerasPresenter @Inject constructor(
  private val cameraSession: CameraSession,
  private val authRepository: IAuthRepository
): MvpPresenter<CamerasView>() {
  private lateinit var controlPanelFragment: ControlPanelFragment
  private lateinit var camerasListFragment: CamerasListFragment
  private lateinit var recordFragment: RecordFragment
  private lateinit var vmixFragment: VmixControlFragment
  private val menuItems: List<MenuItem>
    get() {
      return listOf(
        MenuItem(FragmentType.CONTROL),
        MenuItem(FragmentType.VMIX)
      )
    }
  private lateinit var menuItemAdpapter: MenuItemAdapter
  private var isCameraPickerOpened = false
  private var isMoreOpened = false
  private var currentFragment = FragmentType.RECORD

  private val compositeDisposable = CompositeDisposable()

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.dispose()
  }

  fun onFragmentPicked(type: FragmentType) {
    currentFragment = type
    isMoreOpened = false
    viewState.setMoreVisibility(isMoreOpened)
    when (type) {
      FragmentType.CONTROL -> {
        viewState.setActionBarLabel("MIEMCam")
        viewState.setArrowVisibility(true)
        viewState.setFragment(controlPanelFragment)
      }
      FragmentType.RECORD -> {
        if (isCameraPickerOpened) {
          viewState.closeCameraPicker()
        }
        viewState.setActionBarLabel("MIEMCam")
        viewState.setArrowVisibility(false)
        viewState.setFragment(recordFragment)
      }
      FragmentType.VMIX -> {
        if (isCameraPickerOpened) {
          viewState.closeCameraPicker()
        }
        viewState.setActionBarLabel("MIEMCam")
        viewState.setArrowVisibility(false)
        viewState.setFragment(vmixFragment)
      }
    }
  }

  fun onMorePressed() {
    menuItemAdpapter.menuItems = menuItems
    viewState.updateMenuItems()
    isMoreOpened = !isMoreOpened
    viewState.setMoreVisibility(isMoreOpened)
  }

  fun onStart() {
    val disposable = authRepository.loginToken()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onComplete = ::startUp,
        onError = { viewState.showError() }
      )
    compositeDisposable.add(disposable)
  }

  private fun startUp() {
    controlPanelFragment =
      ControlPanelFragment { viewState.setActionBarLabel("MIEMCam") }

    menuItemAdpapter = MenuItemAdapter(menuItems) { fragment ->
      onFragmentPicked(fragment)
      viewState.selectMoreNavigation()
    }

    camerasListFragment =
      CamerasListFragment(this::setLoadingAnimationVisibility, this::onCameraPicked)

    recordFragment = RecordFragment()
    vmixFragment = VmixControlFragment()

    viewState.setUpViews()
    viewState.setUpMenu(menuItemAdpapter)
    onFragmentPicked(currentFragment)
    viewState.setBackgroundFragment(camerasListFragment)
    viewState.setLoadingVisibility(false)
  }

  fun changeCamerasListVisibility() {
    if (currentFragment != FragmentType.CONTROL) {
      return
    }
    if (!isCameraPickerOpened) {
      camerasListFragment.reload()
      viewState.openCameraPicker()
    } else {
      viewState.closeCameraPicker()
    }
    isCameraPickerOpened = !isCameraPickerOpened
  }

  fun clean() {
    cameraSession.clean()
    viewState.closeCameraPicker()
  }

  private fun onCameraPicked(text: String) {
    viewState.setActionBarLabel(text)
    viewState.setLoadingVisibility(false)
    viewState.closeCameraPicker()
    isCameraPickerOpened = false
    controlPanelFragment.resetView()
  }

  private fun setLoadingAnimationVisibility(isVisible: Boolean) {
    viewState.setLoadingVisibility(isVisible)
  }
}