package ru.hse.miem.miemcam.presentation.cameras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.miem.miemcam.domain.entities.Camera
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.camera.view.*

class CamerasAdapter(
  private var camerasList: List<Camera>,
  private val onItemClicked: ((Camera) -> Unit)
) : RecyclerView.Adapter<CamerasAdapter.CameraHolder>() {

  fun updateList(newList: List<Camera>) {
    camerasList = newList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
      = CameraHolder(LayoutInflater.from(parent.context).inflate(R.layout.camera, parent, false))

  override fun getItemCount() = camerasList.size

  override fun onBindViewHolder(holder: CameraHolder, position: Int) {
    val shouldShowRoom =
      (position == 0) || (camerasList[position].room != camerasList[position - 1].room)
    holder.bind(camerasList[position], shouldShowRoom)
  }

  inner class CameraHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Camera, shouldShowRoom: Boolean) = itemView.apply {
      room.text = if (shouldShowRoom) item.room else ""
      name.text = item.name
      name.setOnClickListener {
        onItemClicked(item)
      }
    }
  }
}