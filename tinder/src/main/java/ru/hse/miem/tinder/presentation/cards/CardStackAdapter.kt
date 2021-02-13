package ru.hse.miem.tinder.presentation.cards

import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ru.hse.miem.tinder.R

class CardStackAdapter(): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private lateinit var items: List<ItemModel>

    constructor(_items: List<ItemModel>): this(){
        items = _items
    }

    @NonNull
    @Override
    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    @Override
    public override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.item_image)
        var nama: TextView = itemView.findViewById(R.id.item_name)
        var usia: TextView = itemView.findViewById(R.id.item_age)
        var kota: TextView = itemView.findViewById(R.id.item_city)

        fun setData(data: ItemModel){
            Picasso.get()
                .load(data.getImage())
                .fit()
                .centerCrop()
                .into(image);
            nama.setText(data.getNama())
            usia.setText(data.getUsia())
            kota.setText(data.getKota())
        }
    }

    public fun getItems(): List<ItemModel>{
        return items
    }

    public fun setItems(_items: List<ItemModel>){
        items = _items
    }

}