package ru.hse.miem.miemapp.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_project.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectBasic

class ProjectsAdapter(private val projects: List<ProjectBasic>) : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(projects[position])
    }

    override fun getItemCount() = projects.size

    class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(project: ProjectBasic) = itemView.apply {
            projectNumber.text = project.number.toString()
            projectName.text = project.name
            projectMembers.text = context.getString(R.string.project_members).format(project.members)
        }
    }
}