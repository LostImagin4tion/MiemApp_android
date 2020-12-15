package ru.hse.miem.miemapp.presentation.apps

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_app.view.*
import ru.hse.miem.miemapp.BuildConfig
import ru.hse.miem.miemapp.R

class AppsAdapter(private val apps: List<AppItem>) : RecyclerView.Adapter<AppsAdapter.AppsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount() = apps.size

    class AppsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(app: AppItem) = itemView.apply {
            appLogo.setImageDrawable(app.icon)
            appName.text = app.name

            setOnClickListener { context.startActivity(Intent().also { it.setClassName(BuildConfig.APPLICATION_ID, app.activityClassName) }) }
        }
    }
}