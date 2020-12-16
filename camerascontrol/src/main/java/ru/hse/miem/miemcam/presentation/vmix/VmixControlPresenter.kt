package ru.hse.miem.miemcam.presentation.vmix

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
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.domain.repositories.IVmixRepository
import javax.inject.Inject

interface VmixControlView : MvpView {
  @AddToEndSingle fun setUpViews()
  @AddToEndSingle fun setState(states: List<List<Boolean>>, animated: Boolean)

  @OneExecution fun showError()
  @OneExecution fun showErrorIncorrectHost()
}

@InjectViewState
class VmixControlPresenter @Inject constructor(
  private val vmixRepository: IVmixRepository
) : MvpPresenter<VmixControlView>() {

  private val compositeDisposable = CompositeDisposable()

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.dispose()
  }

  fun viewCreated() {
    viewState.setUpViews()
    viewState.setState(List(4) { List(6) { false } }, false)
    updateState()
  }

  fun overlayBtnClicked(position: Int, source: Int) {
    vmixRepository.executeFunction("OverlayInput$position", source.toString()).proceed()
  }

  fun programBtnClicked(source: Int) {
    vmixRepository.executeFunction("CutDirect", source.toString()).proceed()
  }

  fun previewBtnClicked(source: Int) {
    vmixRepository.executeFunction("PreviewInput", source.toString()).proceed()
  }

  fun cutBtnClicked() {
    vmixRepository.executeFunction("Cut", null).proceed()
  }

  fun fadeBtnClicked() {
    vmixRepository.executeFunction("Fade", null).proceed()
  }

  fun recBtnClicked() {
    vmixRepository.executeFunction("StartStopRecording", null).proceed()
  }

  fun streamBtnClicked() {
    vmixRepository.executeFunction("StartStopStreaming", null).proceed()
  }

  fun setVmixPressed(adress: String = CameraSession.defaultVmix ) {
    val ip = adress.substringBefore(":")
    try {
      val port = adress.substringAfter(":").toInt()
      vmixRepository.chooseVmix(ip, port).proceed()
    } catch (e: NumberFormatException) {
      viewState.showErrorIncorrectHost()
      return
    }
  }

  private fun updateState() {
    val disposable = vmixRepository.getState()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onSuccess = {
          val state = MutableList(4) { MutableList(6) { false } }
          val validateAndSet: (Int?, Int) -> Unit = { source, position ->
            if (source != null && source - 1 in 0..3) state[source - 1][position] = true
          }
          validateAndSet(it.overlay1?.toIntOrNull(), 0)
          validateAndSet(it.overlay2?.toIntOrNull(), 1)
          validateAndSet(it.overlay3?.toIntOrNull(), 2)
          validateAndSet(it.overlay4?.toIntOrNull(), 3)
          validateAndSet(it.active?.toIntOrNull(), 4)
          validateAndSet(it.preview?.toIntOrNull(), 5)
          viewState.setState(state, true)
        },
        onError = { viewState.showError() }
      )
    compositeDisposable.add(disposable)
  }

  private fun Completable.proceed(onComplete: () -> Unit = ::updateState) {
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