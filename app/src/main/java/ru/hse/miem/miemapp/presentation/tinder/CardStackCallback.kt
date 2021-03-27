package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import android.view.View
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class CardStackCallback : CardStackListener {

    var pos: Int = 0

    override fun onCardDragging(direction: Direction, ratio: Float) {}

    override fun onCardSwiped(direction: Direction) {
        if (direction == Direction.Right) {
            Sorting.likeIndexes.add(pos)
        }
        Log.d("seara", "added:" + Sorting.likeIndexes.toString())
        Sorting.plus()
    }

    override fun onCardRewound() {
        Log.d("seara", "removed:" + Sorting.likeIndexes.toString())
        Sorting.likeIndexes.removeLast()
    }

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View, position: Int) {
        pos = position
    }

    override fun onCardDisappeared(view: View, position: Int) {}
}