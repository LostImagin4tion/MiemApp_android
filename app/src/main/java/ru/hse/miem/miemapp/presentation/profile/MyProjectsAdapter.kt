package ru.hse.miem.miemapp.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_project_my.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.MyProjectBasic
import ru.hse.miem.miemapp.presentation.TextViewUtils.makeNameValueString

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

            projectState.text = project.state
            projectState.setBackgroundResource(if (project.isActive) R.drawable.project_badge_active_bg else R.drawable.project_badge_inactive_bg)

            projectType.text = makeNameValueString(R.string.project_type, project.type)
            projectHead.text = makeNameValueString(R.string.project_head, project.head)
            projectRole.text = makeNameValueString(R.string.project_role, project.role)
            projectMembers.text = makeNameValueString(R.string.project_members, project.members.toString())
            projectHours.text = makeNameValueString(R.string.project_hours, project.hours.toString())
            setOnClickListener { navigateToProject(project.id) }
        }
    }
}