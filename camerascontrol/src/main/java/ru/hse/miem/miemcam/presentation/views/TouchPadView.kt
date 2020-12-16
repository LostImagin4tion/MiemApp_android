package ru.hse.miem.miemcam.presentation.views

import android.content.Context
import android.graphics.*
import android.view.View

class TouchPadView(context: Context?) : View(context) {

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    val paint = Paint()
    paint.strokeWidth = 6f
    paint.color = Color.GRAY
    paint.style = Paint.Style.STROKE
    paint.strokeCap = Paint.Cap.ROUND
    val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
    canvas?.drawRoundRect(rect, 5f, 5f, paint)
    paint.color = Color.WHITE
    canvas?.drawLine(
      width.toFloat()/2 - 20,
      height.toFloat()/2,
      width.toFloat()/2 + 20,
      height.toFloat()/2,
      paint)
    canvas?.drawLine(
      width.toFloat()/2,
      height.toFloat()/2 - 20,
      width.toFloat()/2,
      height.toFloat()/2 + 20,
      paint)
  }
}