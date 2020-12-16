package ru.hse.miem.miemcam.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.miem.miemcam.presentation.util.FragmentType
import ru.hse.miem.miemcam.presentation.util.MenuItem
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.menu_item.view.*

class MenuItemAdapter(
  var menuItems: List<MenuItem>,
  val onItemClicked: (FragmentType) -> (Unit)
) : RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
      = MenuItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false))

  override fun getItemCount() = menuItems.size

  override fun onBindViewHolder(holder: MenuItemHolder, position: Int) {
    holder.bind(menuItems[position])
  }

  inner class MenuItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: MenuItem) = itemView.apply {
      icon.setImageResource(item.icon)
      name.text = itemView.context.getString(item.name)
      itemView.setOnClickListener {
        onItemClicked(item.fragment)
      }
    }
  }
}