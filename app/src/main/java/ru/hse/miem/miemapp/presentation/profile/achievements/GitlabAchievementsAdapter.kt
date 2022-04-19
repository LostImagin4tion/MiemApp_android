package ru.hse.miem.miemapp.presentation.profile.achievements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_achievement_gitlab.view.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Achievements

class GitlabAchievementsAdapter(
    private val achievements: List<Achievements.Gitlab>
): RecyclerView.Adapter<GitlabAchievementsAdapter.AchievementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement_gitlab, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount() = achievements.size

    class AchievementViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(achievement: Achievements.Gitlab) = itemView.apply {

            Glide.with(this)
                .load(achievement.image)
                .into(achievementIcon)

            achievementTitle.text = achievement.name
            achievementCondition.text = achievement.awardCondition

            if (achievement.progress == 100) {
                achievementProgressBar.visibility = View.GONE
            }
            else {
                achievementProgressBar.visibility = View.VISIBLE
                achievementProgressBar.progress = achievement.progress
            }
        }
    }
}