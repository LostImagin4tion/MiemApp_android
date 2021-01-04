package ru.hse.miem.miemapp.presentation.project

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_link.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended

class LinksAdapter(
    private val links: List<ProjectExtended.Link>
) : RecyclerView.Adapter<LinksAdapter.LinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_link, parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(links[position])
    }

    override fun getItemCount() = links.size

    class LinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(link: ProjectExtended.Link) = itemView.apply {
            linkTextView.text = link.name
            setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link.url)))
            }
        }
    }
}