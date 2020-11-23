package ru.hse.miem.miemapp.presentation.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_team_member.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended


class MembersAdapter(
    private val members: List<ProjectExtended.Member>,
    private val navigateToProfile: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(members[position], navigateToProfile)
    }

    override fun getItemCount() = members.size

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(member: ProjectExtended.Member, navigateToProfile: (Long, Boolean) -> Unit) = itemView.apply {
            memberName.text = context.getString(R.string.project_member_name).format(member.firstName, member.lastName)
            memberRole.text = member.role
            Glide.with(this)
                .load(member.avatarUrl)
                .apply(RequestOptions().circleCrop())
                .into(memberAvatar)
            setOnClickListener { navigateToProfile(member.id, member.isTeacher) }
        }
    }
}