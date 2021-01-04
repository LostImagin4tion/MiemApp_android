package ru.hse.miem.miemapp.presentation.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_project_vacancy.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended

class VacanciesAdapter(
    private val vacancies: List<ProjectExtended.Vacancy>
) : RecyclerView.Adapter<VacanciesAdapter.VacancyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_vacancy, parent, false)
        return VacancyHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount() = vacancies.size

    class VacancyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vacancy: ProjectExtended.Vacancy) = itemView.apply {
            vacancyName.text = vacancy.role
            vacancyCount.text = vacancy.count.toString()
            vacancyRequired.text = vacancy.required.takeIf { it.isNotEmpty() } ?: resources.getString(R.string.no_data)
            vacancyRecommended.text = vacancy.recommended.takeIf { it.isNotEmpty() } ?: resources.getString(R.string.no_data)
        }
    }

}