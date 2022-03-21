package ru.hse.miem.miemapp.presentation.tinder

import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
import android.view.ViewGroup;
import kotlinx.android.synthetic.main.item_cards.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.tagsList

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
            if (resources.displayMetrics.widthPixels < 720
                    && data.projectNameRus != resources.getString(R.string.tinder_welcome)){
                projectTitle.maxLines = 2
            }else if (resources.displayMetrics.widthPixels < 1000
                    && data.projectNameRus != resources.getString(R.string.tinder_welcome)){
                projectTitle.maxLines = 4
            }
            vacancyTitle.text = data.vacancyRole

            val buttons = listOf(t1, t2, t3, t4, t5, t6)
            buttons.forEach {
                it.visibility = View.GONE
            }

            var tags = mutableSetOf<String>()
            tagsList.takeWhile { tags.size < 6 }.forEach {
                if (data.requirements.indexOf(it, ignoreCase = true) != -1 && tags.size < 6) {
                    tags.add(it)
                }
                if (data.vacancyRole.indexOf(it, ignoreCase = true) != -1 && tags.size < 6) {
                    tags.add(it)
                }
                if (data.projectNameRus.indexOf(it, ignoreCase = true) != -1 && tags.size < 6) {
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