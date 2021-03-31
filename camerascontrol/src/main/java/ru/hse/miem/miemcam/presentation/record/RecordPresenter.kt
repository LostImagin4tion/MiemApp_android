package ru.hse.miem.miemcam.presentation.record

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemcam.domain.repositories.IRecordRepository
import timber.log.Timber
import javax.inject.Inject

interface RecordView : MvpView {
  @AddToEndSingle fun setUpRoomsListView(roomsAdapter: RoomsAdapter)
  @AddToEndSingle fun updateRoomsListView()
  @AddToEndSingle fun stopLoadAnimation()
  @AddToEndSingle fun showTimePicker(initialTime: Pair<Int, Int>, completion: (Pair<Int, Int>) -> Unit)
  @AddToEndSingle fun showDatePicker(initialDate: Long, completion: (Long) -> Unit)
  @AddToEndSingle fun showCreateDialog(defaultEmail: String, completion: (String, String) -> Unit)

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun showError()

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun showRequestRecordResponse()
}

@InjectViewState
class RecordPresenter @Inject constructor(
  private val recordRepository: IRecordRepository,
  private val session: Session
) : MvpPresenter<RecordView>() {
  private var roomsList = listOf<String>()
  private val roomsAdapter =
    RoomsAdapter(
      roomsList,
      viewState::showTimePicker,
      viewState::showDatePicker,
      ::onRequestClicked
    )

  private val compositeDisposable = CompositeDisposable()

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.dispose()
  }

  fun viewCreated() {
    val disposable = recordRepository.getRooms()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onSuccess = ::onRoomsReceived,
        onError = {
          Timber.e(it.stackTraceToString())
          viewState.showError()
        }
      )
    compositeDisposable.add(disposable)
  }

  private fun onRequestClicked(room: String, date: String, start: String, stop: String) {
    viewState.showCreateDialog(session.email) { email, name ->
      recordRepository.requestRecord(room, email, name, date, start, stop)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
          onComplete = { viewState.showRequestRecordResponse() },
          onError = { viewState.showError() }
        )
      viewCreated()
    }
  }

  private fun onRoomsReceived(rooms: List<String>) {
    roomsList = rooms.sorted()
    roomsAdapter.updateList(roomsList)
    viewState.stopLoadAnimation()
    viewState.updateRoomsListView()

    viewState.setUpRoomsListView(roomsAdapter)
    if (roomsList.isNotEmpty()) {
      viewState.stopLoadAnimation()
    }
  }
}