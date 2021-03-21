package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ru.hse.miem.miemapp.R
import java.util.ArrayList

class CardStackAdapter(): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private var items: List<ItemModel> = emptyList()
    var screenItems: ArrayList<ItemModel> = arrayListOf()

    constructor(_items: List<ItemModel>): this(){
        items = _items
    }

    val hadData get() = items.isNotEmpty()

    @NonNull
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cards, parent, false)
        return ViewHolder(view)
    }

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
        screenItems.add(items[position])
    }

    @Override
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var image: ImageView = itemView.findViewById(R.id.item_image)
        var type: TextView = itemView.findViewById(R.id.item_type)
        var name: TextView = itemView.findViewById(R.id.item_name)
        var vacancy: TextView = itemView.findViewById(R.id.item_vacancy)
//        var requirements: TextView = itemView.findViewById(R.id.item_requirements)
        var leader: TextView = itemView.findViewById(R.id.item_leader)
        var text1: Button = itemView.findViewById(R.id.tag1)
        var text2: Button = itemView.findViewById(R.id.tag2)

        fun setData(data: ItemModel){
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
            if (data.requirements.indexOf("C++") != -1){
                text1.text = "C++"
            }
            if (data.requirements.indexOf("Python") != -1){
                text2.text = "Python"
            }
        }
    }
}