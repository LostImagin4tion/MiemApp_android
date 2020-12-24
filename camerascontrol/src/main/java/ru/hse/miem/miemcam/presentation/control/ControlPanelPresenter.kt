package ru.hse.miem.miemcam.presentation.control

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemcam.data.repositories.*
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.domain.repositories.FocusMode
import ru.hse.miem.miemcam.domain.repositories.IActionRepository
import ru.hse.miem.miemcam.domain.repositories.IPresetRepository
import ru.hse.miem.miemcam.domain.repositories.SettingType
import java.lang.Math.abs
import javax.inject.Inject

interface ControlPanelView : MvpView {
  @AddToEndSingle fun changeIsEnabledFocusAutoBtn(isEnabled: Boolean)
  @AddToEndSingle fun changeIsEnabledFocusManualBtn(isEnabled: Boolean)
  @AddToEndSingle fun startStream(uri: String)
  @AddToEndSingle fun releaseCamera()

  @OneExecution fun showError()
}

@InjectViewState
class ControlPanelPresenter @Inject constructor(
  private val actionRepository: IActionRepository,
  private val presetRepository: IPresetRepository,
  private val cameraServices: CameraRepository,
  private val cameraSession: CameraSession
) : MvpPresenter<ControlPanelView>() {
  private var prevX = 2f
  private var prevY = 2f
  private var currentFocusMode = FocusMode.AUTO

  private val compositeDisposable = CompositeDisposable()

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.dispose()
  }

  fun stopped() {
    actionRepository.stop().proceed {
      prevX = 2f
      prevY = 2f
    }
  }

  fun presetBtnClicked(id: Int) {
    presetRepository.execPreset(id).proceed()
  }

  fun presetBtnLongClicked(id: Int) {
    presetRepository.setPreset(id).proceed()
  }

  fun zoomBtnClicked(isIncreasing: Boolean) {
    val zoom = if (isIncreasing) {
      0.5f
    } else {
      -0.5f
    }
    actionRepository.moveCam(arrayListOf(0f, 0f, zoom)).proceed()
  }

  fun touchPadTouched(x: Float, y: Float) {
    if (abs(x  - prevX) > 0.05 || abs(y  - prevY) > 0.05 || prevX == 2f) {
      prevX = x
      prevY = y
      actionRepository.moveCam(arrayListOf(x, y, 0f)).proceed()
    }
  }

  fun setSetting(type: SettingType, value: Float) {
    actionRepository.setSetting(type, value).proceed()
  }

  fun focusBtnClicked(isIncreasing: Boolean) {
    val focus = if (isIncreasing) {
      0.5f
    } else {
      -0.5f
    }
    actionRepository.setFocusContinious(focus).proceed()
  }

  fun focusStopped() {
    actionRepository.stopFocus().proceed()
  }

  fun setFocusMode(mode: FocusMode) {
    actionRepository.setFocusMode(mode).proceed {
      currentFocusMode = mode
      updateFocusModeBtns()
    }
  }

  fun startStream() {
    val cameraStream = cameraSession.pickedCamera
    if (cameraStream.isNotEmpty()) {
      viewState.startStream(cameraStream)
    }
  }

  fun viewDestroyed() {
    cameraSession.pickedCamera = ""
    cameraSession.pickedRoom = ""
    viewState.releaseCamera()
    cameraServices.releaseCamera()
  }

  private fun updateFocusModeBtns() {
    when (currentFocusMode) {
      FocusMode.AUTO -> {
        viewState.changeIsEnabledFocusAutoBtn(true)
        viewState.changeIsEnabledFocusManualBtn(false)
      }
      FocusMode.MANUAL -> {
        viewState.changeIsEnabledFocusAutoBtn(false)
        viewState.changeIsEnabledFocusManualBtn(true)
      }
    }
  }

  private fun Completable.proceed(onComplete: () -> Unit = {}) {
    compositeDisposable.add(
      subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
          onComplete = onComplete,
          onError = { viewState.showError() }
        )
    )
  }
}