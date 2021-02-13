package ru.hse.miem.tinder.presentation.cards

import androidx.recyclerview.widget.DiffUtil

class CardStackCallback(_old: List<ItemModel>, _baru: List<ItemModel>): DiffUtil.Callback() {
    var old: List<ItemModel> = _old
    var baru: List<ItemModel> = _baru

    @Override
    public override fun getOldListSize(): Int{
        return old.size
    }

    @Override
    public override fun getNewListSize(): Int{
        return baru.size
    }

    @Override
    public override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old.get(oldItemPosition).getImage() == baru.get(newItemPosition).getImage();
    }

    @Override
    public override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}