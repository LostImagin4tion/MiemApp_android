package ru.hse.miem.miemapp.presentation.tinder

import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.item_cards.view.*
import kotlinx.android.synthetic.main.item_project_in_search.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.tagsList
import ru.hse.miem.miemapp.presentation.TextViewUtils.makeNameValueString
import java.util.ArrayList

class CardStackAdapter(private var items: List<VacancyCard>) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    val hadData get() = items.isNotEmpty()

    @NonNull
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cards, parent, false)
        return ViewHolder(view)
    }

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @Override
    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: VacancyCard) = itemView.apply {
            projectId.text = data.projectId
            projectTitle.text = data.projectNameRus
            vacancyTitle.text = data.vacancyRole

            val buttons = listOf(t1, t2, t3, t4, t5, t6)
            buttons.forEach {
                it.visibility = View.GONE
            }

            var tags = mutableSetOf<String>()
            tagsList.takeWhile { tags.size < 6 }.forEach {
                if (data.requirements.indexOf(it, ignoreCase = true) != -1) {
                    tags.add(it)
                }
                if (data.vacancyRole.indexOf(it, ignoreCase = true) != -1) {
                    tags.add(it)
                }
                if (data.projectNameRus.indexOf(it, ignoreCase = true) != -1) {
                    tags.add(it)
                }
            }

            tags.forEachIndexed { i, element ->
                buttons[i].visibility = View.VISIBLE
                buttons[i].text = element
            }
        }
    }
}