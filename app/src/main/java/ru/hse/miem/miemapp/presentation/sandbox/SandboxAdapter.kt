package ru.hse.miem.miemapp.presentation.sandbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_project_in_sandbox.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectInSandbox
import ru.hse.miem.miemapp.presentation.TextViewUtils.makeNameValueString
import java.util.*

class SandboxAdapter(
    private val navigateToProject: (Long) -> Unit
): RecyclerView.Adapter<SandboxAdapter.ProjectViewHolder>() {

    private var projects: List<ProjectInSandbox> = emptyList()
    private var displayedProjects = projects

    val hasData get() = projects.isNotEmpty()

    fun performSearch(condition: String, filters: SandboxFilters) {
        fun String.matchesCondition() = toLowerCase(Locale.getDefault()).matches(Regex(".*$condition.*"))

        displayedProjects = projects.filter {
                it.name.matchesCondition() ||
                it.head.matchesCondition() ||
                condition.isEmpty() || condition.isBlank()
            }
            .filter { !filters.isAvailableVacancies || it.vacancies > 0 }
            .filter { filters.projectType == 0 || it.type == filters.projectTypeName } // filter if not default
            .filter { filters.projectState == 0 || it.state == filters.projectStateName }

        notifyDataSetChanged()
    }

    fun update(projects: List<ProjectInSandbox>) {
        this.projects = projects
        if(displayedProjects.isEmpty()) {
            displayedProjects = this.projects
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_in_sandbox, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(displayedProjects[position], navigateToProject)
    }

    override fun getItemCount() = displayedProjects.size

    class ProjectViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(project: ProjectInSandbox, navigateToProject: (Long) -> Unit) = itemView.apply {
            projectName.text = project.name
            projectState.setBackgroundResource(R.drawable.badge_inactive_bg)
            projectState.text = project.state
            project.vacancies.takeIf { it > 0 }?.let {
                projectVacancies.visibility = View.VISIBLE
                projectVacancies.text = context.getString(R.string.project_vacancies).format(project.vacancies)
            }
            projectType.text = makeNameValueString(R.string.project_type, project.type)
            projectHead.text = makeNameValueString(R.string.project_head, project.head)

            setOnClickListener { navigateToProject(project.id) }
        }
    }
}