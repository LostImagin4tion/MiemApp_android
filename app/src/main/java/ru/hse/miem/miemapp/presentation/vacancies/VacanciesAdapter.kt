package ru.hse.miem.miemapp.presentation.vacancies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_project_in_search.view.*
import kotlinx.android.synthetic.main.item_vacancy_in_viewall.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.tagsList
import java.util.ArrayList

class VacanciesAdapter (
    private val navigateToProject: (Long) -> Unit
) : RecyclerView.Adapter<VacanciesAdapter.VacancyViewHolder>() {

    private var vacancies: List<VacancyCard> = emptyList()

    val hasData get() = vacancies.isNotEmpty()

    fun update(projects: List<VacancyCard>) {
        this.vacancies = projects
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_vacancy, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position], navigateToProject)
    }

    override fun getItemCount() = vacancies.size

    class VacancyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vacancy: VacancyCard, navigateToProject: (Long) -> Unit) = itemView.apply {
            Log.d("vacancy",vacancy.project_id)
            projectId.text = vacancy.project_id
            projectTitle.text = vacancy.project_name_rus
            vacancyName.text = vacancy.vacancy_role

            val buttons = arrayListOf(tag1, tag2, tag3)
            buttons.forEach {
                it.visibility = View.GONE
            }

            var tags: ArrayList<String> = arrayListOf()
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
            setOnClickListener { navigateToProject(java.lang.Long.parseLong(vacancy.project_id)) }
        }
    }

}