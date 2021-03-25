package ru.hse.miem.miemapp.presentation.tinder

import android.view.View
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import java.util.ArrayList


class CardStackCallback: CardStackListener {
    companion object {
        val likeVacancy: ArrayList<Int> = arrayListOf()
    }
    var pos: Int = 0
    private var sorting = Sorting()

    fun getLikedVacancies() : ArrayList<Int>{
        return likeVacancy
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {}

    override fun onCardSwiped(direction: Direction) {
        if (direction == Direction.Right && pos != 0){
            likeVacancy.add(pos)
        }
        sorting.plus()
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View, position: Int) {
        pos = position
    }

    override fun onCardDisappeared(view: View, position: Int) {}
}