package ru.hse.miem.miemcam.presentation.record

import android.app.TimePickerDialog
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.fragment_record.*
import ru.hse.miem.miemcam.presentation.main.CamerasActivity
import javax.inject.Inject

class RecordFragment : MvpAppCompatFragment(), RecordView {

  @Inject
  @InjectPresenter
  lateinit var recordPresenter: RecordPresenter

  @ProvidePresenter
  fun provideRecordPresenter() = recordPresenter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_record, container, false)
  }

  override fun onAttach(context: Context) {
    (activity as CamerasActivity).camerasComponent.inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recordPresenter.viewCreated()
  }

  override fun setUpRoomsListView(roomsAdapter: RoomsAdapter) {
    val viewManager = LinearLayoutManager(this.context)
    roomsListView.layoutManager = viewManager
    roomsListView.adapter = roomsAdapter
  }

  override fun updateRoomsListView() {
    activity?.runOnUiThread {
      roomsListView?.adapter?.notifyDataSetChanged()
    }
  }

  override fun stopLoadAnimation() {
    activity?.runOnUiThread {
      loadingProgressList?.visibility = View.INVISIBLE
    }
  }

  override fun showTimePicker(initialTime: Pair<Int, Int>, completion: (Pair<Int, Int>) -> Unit) {
    val timeListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
      completion(Pair(selectedHour, selectedMinute))
    }
    TimePickerDialog(context, R.style.TimePicker, timeListener, initialTime.first, initialTime.second, true).show()
  }

  override fun showDatePicker(initialDate: Long, completion: (Long) -> Unit) {
    val builder = MaterialDatePicker.Builder.datePicker()
    builder.setTitleText("Выберите дату")
    builder.setSelection(initialDate)
    val picker = builder.build()
    picker.addOnPositiveButtonClickListener {
      completion(it)
    }
    picker.addOnNegativeButtonClickListener {
      picker.dismiss()
    }
    picker.show(activity?.supportFragmentManager!!, picker.toString())
  }

  override fun showCreateDialog(defaultEmail: String, completion: (String, String) -> Unit) {
    val inputView = View.inflate(context, R.layout.record_info, null)
    val cutName = inputView.findViewById<TextInputEditText>(R.id.cutName)
    val email = inputView.findViewById<TextInputEditText>(R.id.email)
    email.setText(defaultEmail)
    MaterialAlertDialogBuilder(requireContext())
      .setTitle("Создать запись?")
      .setView(inputView)
      .setPositiveButton("OK") { _, _ ->
        completion(cutName.text.toString(), email.text.toString())
      }
      .setNegativeButton("Отменить") { dialog, _ ->
        dialog.dismiss()
      }
      .create()
      .show()
  }

  override fun showError() {
    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
  }

  override fun showRequestRecordResponse() {
    Toast.makeText(requireContext(), R.string.request_record_response, Toast.LENGTH_SHORT).show()
  }
}
