package ru.hse.miem.miemapp.presentation.vacancies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_vacancy_in_tinder.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.tagsList
import ru.hse.miem.miemapp.presentation.tinder.Sorting

class VacanciesAdapter(
    private val navigateToProject: (Long) -> Unit
) : RecyclerView.Adapter<VacanciesAdapter.VacancyViewHolder>() {

    private var vacancies: MutableList<VacancyCard> = mutableListOf()

    val hasData get() = vacancies.isNotEmpty()

    fun update(vacancies: List<VacancyCard>) {
        for (item in Sorting.likeVacancies){
            if (item in vacancies && item !in this.vacancies){
                this.vacancies.add(item)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vacancy_in_tinder, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position], navigateToProject)
    }

    override fun getItemCount() = vacancies.size

    class VacancyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vacancy: VacancyCard, navigateToProject: (Long) -> Unit) = itemView.apply {
            projectId.text = vacancy.projectId
            projectTitle.text = vacancy.projectNameRus
            vacancyTitle.text = vacancy.vacancyRole

            val buttons = arrayListOf(t1, t2, t3)
            buttons.forEach {
                it.visibility = View.GONE
            }

            val tags = mutableListOf<String>()
            for (temp_tag in tagsList) {
                if ((vacancy.requirements).indexOf(temp_tag, ignoreCase = true) != -1) {
                    tags.add(temp_tag)
                }
                if (tags.size == 3) {
                    break
                }
            }

            for (i in tags.indices) {
                buttons[i].visibility = View.VISIBLE
                buttons[i].text = tags[i]

            }
            setOnClickListener { navigateToProject(vacancy.projectId.drop(1).toLong()) }
        }
    }
}