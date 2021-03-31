package ru.hse.miem.miemcam.presentation.cameras

import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemcam.domain.entities.Camera
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.domain.repositories.ICameraRepository
import javax.inject.Inject

interface CamerasListView : MvpView {
  @AddToEndSingle fun setToolbarLabel(text: String)
  @AddToEndSingle fun setLoadingVisibility(isVisible: Boolean)
  @AddToEndSingle fun setUpCamerasListView(camerasAdapter: CamerasAdapter)
  @AddToEndSingle fun updateCamerasListView()
  @AddToEndSingle fun stopLoadAnimation()

  @OneExecution fun showError()
}

@InjectViewState
class CamerasListPresenter @Inject constructor(
  private val cameraServices: ICameraRepository,
  private val cameraSession: CameraSession
) : MvpPresenter<CamerasListView>() {

  private var camerasList = mutableListOf<Camera>()
  private val camerasAdapter =
    CamerasAdapter(
      camerasList,
      ::onCameraPicked
    )

  private val compositeDisposable = CompositeDisposable()

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.dispose()
  }

  fun onViewCreated() {
    reload()
    viewState.setUpCamerasListView(camerasAdapter)
  }

  fun reload() {
    val disposable = cameraServices.discovery()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onSuccess = ::onCamerasReceived,
        onError = { viewState.showError() }
      )
    compositeDisposable.add(disposable)
  }

  fun clearList() {
    camerasList.clear()
  }

  private fun onCameraPicked(camera: Camera) {
    cameraSession.apply {
      pickedRoom = camera.room
      pickedCamera = camera.rtsp
    }
    viewState.setToolbarLabel(camera.name)
    val disposable = cameraServices.chooseCam(camera.uid)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onComplete = {  },
        onError = { viewState.showError() }
      )
    compositeDisposable.add(disposable)

  }

  private fun onCamerasReceived(cameras: List<Camera>) {
    this.camerasList = cameras.sortedBy { it.room }.toMutableList()
    camerasAdapter.updateList(camerasList)
    viewState.stopLoadAnimation()
    viewState.updateCamerasListView()
  }
}