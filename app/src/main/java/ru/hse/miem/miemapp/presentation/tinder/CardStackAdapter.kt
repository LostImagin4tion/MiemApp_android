package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button
import android.widget.TextView
import com.squareup.picasso.Picasso
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ItemModel
import ru.hse.miem.miemapp.domain.entities.tagsList
import java.util.ArrayList

class CardStackAdapter() : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private var sorting = Sorting

    var items: List<ItemModel> = emptyList()

    constructor(_items: List<ItemModel>) : this() {
        Log.d("tinder", "Constructor " + sorting.position)
        items = _items
    }

    val hadData get() = items.isNotEmpty()

    @NonNull
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cards, parent, false)
        return ViewHolder(view)
    }

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    @Override
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sorting: Sorting = Sorting()

        //        var image: ImageView = itemView.findViewById(R.id.item_image)
        var type: TextView = itemView.findViewById(R.id.item_type)
        var name: TextView = itemView.findViewById(R.id.item_name)
        var vacancy: TextView = itemView.findViewById(R.id.item_vacancy)

        //        var requirements: TextView = itemView.findViewById(R.id.item_requirements)
        var leader: TextView = itemView.findViewById(R.id.item_leader)
        var text1: Button = itemView.findViewById(R.id.tag1)
        var text2: Button = itemView.findViewById(R.id.tag2)
        var text3: Button = itemView.findViewById(R.id.tag3)
        var text4: Button = itemView.findViewById(R.id.tag4)
        var text5: Button = itemView.findViewById(R.id.tag5)
        var text6: Button = itemView.findViewById(R.id.tag6)
        fun setData(data: ItemModel) {
            Picasso.get()
                .load(data.image)
                .fit()
                .centerCrop()
//                .into(image);
            type.text = data.type
            name.text = data.name
            vacancy.text = data.vacancy
//            requirements.setText(data.requirements)
            leader.text = data.leader

            val buttons = arrayListOf(text1, text2, text3, text4, text5, text6)
            buttons.forEach {
                it.visibility = View.GONE
            }

            var tags: ArrayList<String> = arrayListOf()
            for (temp_tag in tagsList) {
                if (data.requirements.indexOf(temp_tag, ignoreCase = true) != -1) {
                    tags.add(temp_tag)
                }
                if (tags.size == 6) {
                    break
                }
            }

            for (i in tags.indices) {
                buttons[i].visibility = View.VISIBLE
                buttons[i].text = tags[i]
            }
        }
    }
}