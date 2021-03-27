package ru.hse.miem.miemapp.presentation.tinder

import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button
import android.widget.TextView
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.tagsList
import java.util.ArrayList

class CardStackAdapter() : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    var items: List<VacancyCard> = emptyList()

    constructor(_items: List<VacancyCard>) : this() {
        items = _items
    }

    val hadData get() = items.isNotEmpty()

    @NonNull
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cards, parent, false)
        return ViewHolder(view)
    }

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    @Override
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var type: TextView = itemView.findViewById(R.id.projectId)
        var name: TextView = itemView.findViewById(R.id.projectTitle)
        var vacancy: TextView = itemView.findViewById(R.id.vacancyTitle)
        var tag1: Button = itemView.findViewById(R.id.t1)
        var tag2: Button = itemView.findViewById(R.id.t2)
        var tag3: Button = itemView.findViewById(R.id.t3)
        var tag4: Button = itemView.findViewById(R.id.t4)
        var tag5: Button = itemView.findViewById(R.id.t5)
        var tag6: Button = itemView.findViewById(R.id.t6)
        fun setData(data: VacancyCard) {
            type.text = data.project_id
            name.text = data.project_name_rus
            vacancy.text = data.vacancy_role

            val buttons = arrayListOf(tag1, tag2, tag3, tag4, tag5, tag6)
            buttons.forEach {
                it.visibility = View.GONE
            }

            var tags: MutableSet<String> = mutableSetOf()
            for (temp_tag in tagsList) {
                if (data.requirements.indexOf(temp_tag, ignoreCase = true) != -1) {
                    tags.add(temp_tag)
                }
                if (data.vacancy_role.indexOf(temp_tag, ignoreCase = true) != -1) {
                    tags.add(temp_tag)
                }
                if (tags.size == 6) {
                    break
                }
            }
            tags.forEachIndexed { i, element ->
                buttons[i].visibility = View.VISIBLE
                buttons[i].text = element
            }
        }
    }
}