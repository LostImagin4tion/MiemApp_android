package ru.hse.miem.miemapp.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_project_my.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.MyProjectBasic

class MyProjectsAdapter(
    private val projects: List<MyProjectBasic>,
    private val navigateToProject: (Long) -> Unit
) : RecyclerView.Adapter<MyProjectsAdapter.MyProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_my, parent, false)
        return MyProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyProjectViewHolder, position: Int) {
        holder.bind(projects[position], navigateToProject)
    }

    override fun getItemCount() = projects.size

    class MyProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(project: MyProjectBasic, navigateToProject: (Long) -> Unit) = itemView.apply {
            projectNumber.text = project.number.toString()
            projectName.text = project.name
            projectState.text = context.getString(R.string.project_state).format(project.state)
            projectType.text = context.getString(R.string.project_type).format(project.type)
            projectHead.text = context.getString(R.string.project_head).format(project.head)
            projectRole.text = context.getString(R.string.project_role).format(project.role)
            projectMembers.text = context.getString(R.string.project_members).format(project.members)
            projectHours.text = context.getString(R.string.project_hours).format(project.hours)
            setOnClickListener { navigateToProject(project.id) }
        }
    }
}