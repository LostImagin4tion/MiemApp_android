package ru.hse.miem.miemapp.presentation.tinder

import ru.hse.miem.miemapp.presentation.search.SearchFilters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_project_in_search.view.*
import kotlinx.android.synthetic.main.item_project_in_search.view.projectName
import kotlinx.android.synthetic.main.item_vacancy_in_viewall.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.domain.entities.tagsList
import java.util.*

class ViewAllAdapter(
    private val navigateToProject: (Long) -> Unit
) : RecyclerView.Adapter<ViewAllAdapter.VacancyViewHolder>() {

    private var vacancies: List<Vacancies> = emptyList()
    private var displayedVacancies = vacancies

    val hasData get() = vacancies.isNotEmpty()

    fun update(projects: List<Vacancies>) {
        this.vacancies = projects
        if (displayedVacancies.isEmpty()) {
            displayedVacancies = this.vacancies
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_vacancy, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(displayedVacancies[position], navigateToProject)
    }

    override fun getItemCount() = displayedVacancies.size

    class VacancyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vacancy: Vacancies, navigateToProject: (Long) -> Unit) = itemView.apply {
            projectId.text = vacancy.project_id.toString()
            projectName.text = vacancy.project_name_rus
            vacancyName.text = vacancy.vacancy_role

            val buttons = arrayListOf(tag1, tag2, tag3)
            buttons.forEach {
                it.visibility = View.GONE
            }

            var tags: ArrayList<String> = arrayListOf()
            for (temp_tag in tagsList) {
                if ((vacancy.vacancy_disciplines.toString() + vacancy.vacancy_additionally).indexOf(temp_tag, ignoreCase = true) != -1) {
                    tags.add(temp_tag)
                }
                if (tags.size == 3) {
                    break
                }
            }

            for (i in tags.indices) {
                buttons[i].visibility = View.VISIBLE
                buttons[i].text = tags[i]


//                projectLeader.text = vacancy.projectLeader


                setOnClickListener { navigateToProject(vacancy.project_id) }
            }
        }
    }
}