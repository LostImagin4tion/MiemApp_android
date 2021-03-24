package ru.hse.miem.miemapp.presentation.tinder

import android.R
import android.util.ArraySet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.collection.arraySetOf
import androidx.recyclerview.widget.DiffUtil
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import java.lang.Exception
import java.util.ArrayList


class CardStackCallback: CardStackListener {
    public val likeVacancy: ArrayList<Int> = arrayListOf()
    var pos: Int = 0
    private var sorting = Sorting()
    private val TAG = "LogTinderAct"

//    fun getLikedVacancies() : ArrayList<Int>{
//        if (likeVacancy.size !=0){
//            return likeVacancy
//    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
//        Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
    }

    override fun onCardSwiped(direction: Direction) {
//        Log.d(TAG, "onCardSwiped: " )
        if (direction == Direction.Right && pos != 0){
            likeVacancy.add(pos)
        }
        sorting.plus()
    }

    override fun onCardRewound() {
//        Log.d(TAG, "onCardRewound: " )
    }

    override fun onCardCanceled() {
//        Log.d(TAG, "onCardRewound: ")
    }

    override fun onCardAppeared(view: View, position: Int) {
//        Log.d(TAG, "onCardAppeared: $position")
        pos = position
    }

    override fun onCardDisappeared(view: View, position: Int) {
//        Log.d(TAG, "onCardAppeared: $position")
    }
}