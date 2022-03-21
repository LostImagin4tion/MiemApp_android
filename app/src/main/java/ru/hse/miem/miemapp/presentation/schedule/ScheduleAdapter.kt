package ru.hse.miem.miemapp.presentation.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.hse.miem.miemapp.domain.entities.IScheduleItem

class ScheduleAdapter: RecyclerView.Adapter<ViewHolder>() {

    private var scheduleItems: MutableList<IScheduleItem> = mutableListOf()
    private var displayedItems = scheduleItems

    val hasData get() = scheduleItems.isNotEmpty()

    fun updateWhenScrolledDown(newItems: List<IScheduleItem>) {
        scheduleItems.addAll(newItems)
        displayedItems = scheduleItems

        notifyDataSetChanged()
    }

    fun update(items: List<IScheduleItem>) {

        this.scheduleItems = if (items.isEmpty()) {
            mutableListOf()
        } else {
            items as MutableList<IScheduleItem>
        }

        displayedItems = this.scheduleItems

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ScheduleViewHolderFactory().create(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        displayedItems[position].onBindViewHolder(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return scheduleItems[position].getItemViewType()
    }

    override fun getItemCount() = displayedItems.size
}