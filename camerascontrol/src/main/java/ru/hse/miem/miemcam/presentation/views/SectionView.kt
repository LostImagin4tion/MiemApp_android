package ru.hse.miem.miemcam.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class SectionView : HorizontalScrollView {
  interface OnScrollStoppedListener {
    fun onScrollStopped()
  }

  private var scrollerTask: Runnable? = null
  private var initialPosition: Int = 0
  private val newCheck = 100L
  private var onScrollStoppedListener: OnScrollStoppedListener? = null

  constructor(context: Context) : super(context) {
    setUp()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    setUp()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    setUp()
  }

  private fun setUp() {
    scrollerTask = Runnable {
      val newPosition = scrollX
      if (initialPosition - newPosition == 0) {
        onScrollStoppedListener?.onScrollStopped()
      } else {
        initialPosition = scrollX
        this.postDelayed(scrollerTask, newCheck)
      }
    }
  }

  fun setOnScrollStoppedListener(listener: OnScrollStoppedListener) {
    onScrollStoppedListener = listener
  }

  fun startScrollerTask() {
    initialPosition = scrollX
    this.postDelayed(scrollerTask, newCheck)
  }
}
