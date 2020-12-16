package ru.hse.miem.miemcam.presentation.record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import ru.hse.miem.miemcam.R
import java.util.*
import kotlinx.android.synthetic.main.room.view.*
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

typealias RecordRequest = ((room: String, date: String, start: String, stop: String) -> Unit)

class RoomsAdapter(
  private var roomsList: List<String>,
  private val showTimePicker: (Pair<Int, Int>, (Pair<Int, Int>) -> Unit) -> Unit,
  private val showDatePicker: (Long, (Long) -> Unit) -> Unit,
  private val onRequestClicked: (RecordRequest)
) : RecyclerView.Adapter<RoomsAdapter.RoomHolder>() {

  fun updateList(newList: List<String>) {
    roomsList = newList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
      = RoomHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.room, parent, false))

  override fun getItemCount() = roomsList.size

  override fun onBindViewHolder(holder: RoomHolder, position: Int) {
    holder.bind(roomsList[position])
  }

  inner class RoomHolder(var context: Context?, itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var fromTime = Pair(0, 0)
    private var toTime = Pair(0, 0)
    private var date: Long

    init {
      val calendar = Calendar.getInstance()
      calendar.set(
        Calendar.HOUR_OF_DAY,
        TimeUnit.HOURS.convert(calendar.timeZone.rawOffset.toLong(), TimeUnit.MILLISECONDS).toInt()
      )
      calendar.set(Calendar.MINUTE, 0)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)
      date = calendar.timeInMillis
    }

    fun bind(item: String) = itemView.apply {
      name.text = item
      setUpDate()
      setUpFromTimeLabel()
      setUpToTimeLabel()
      setUpCreateBtn()
    }

    private fun setUpDate() = itemView.apply {
      setDateLabel()
      dateTime.setOnClickListener {
        showDatePicker(date) {
          date = it
          setDateLabel()
        }
      }
    }

    private fun setUpFromTimeLabel() = itemView.apply {
      startTime.setOnClickListener {
        showTimePicker(fromTime) {
          fromTime = it
          setTimeLabel(startTime, fromTime)
        }
      }
    }

    private fun setUpToTimeLabel() = itemView.apply {
      stopTime.setOnClickListener {
        showTimePicker(toTime) {
          toTime = it
          setTimeLabel(stopTime, toTime)
        }
      }
    }

    private fun setUpCreateBtn() = itemView.apply {
      create.setOnClickListener {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        onRequestClicked(
          name.text.toString(),
          format.format(Date(date)),
          startTime.text.toString(),
          stopTime.text.toString()
        )
      }
    }

    private fun setDateLabel() = itemView.apply {
      val format = SimpleDateFormat("dd.MM.yyyy", Locale.UK)
      dateTime.text = format.format(Date(date))
    }

    private fun setTimeLabel(label: TextView, time: Pair<Int, Int>) {
      val format: (Int) -> String = {
        String.format("%02d", it)
      }
      val timeFormatted = "${format(time.first)}:${format(time.second)}"
      label.text = timeFormatted
    }
  }
}