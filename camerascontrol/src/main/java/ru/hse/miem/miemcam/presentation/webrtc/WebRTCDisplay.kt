package ru.hse.miem.miemcam.presentation.webrtc

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.webkit.WebView

class WebRTCDisplay: WebView {

  private val scaleDetector: ScaleGestureDetector
  var maxZoom = 1f
    set(value) {
      field = value + .01f
    }
  var currentZoom = 1f

  constructor(context: Context): super(context) {
    scaleDetector = ScaleGestureDetector(context,
      ScaleListener(::setZoom)
    )
  }

  constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
    scaleDetector = ScaleGestureDetector(context,
      ScaleListener(::setZoom)
    )
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
    scaleDetector = ScaleGestureDetector(context,
      ScaleListener(::setZoom)
    )
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(ev: MotionEvent?): Boolean {
    scaleDetector.onTouchEvent(ev)
    return true
  }

  fun resetZoom() {
    val prevZoom = currentZoom
    currentZoom = 1f
    this.zoomBy(1 / prevZoom)
  }

  private fun setZoom(zoomFactor: Float) {
    if (currentZoom * zoomFactor < 1) {
      val prevZoom = currentZoom
      currentZoom = 1f
      this.zoomBy(1 / prevZoom)
    } else if (currentZoom * zoomFactor > maxZoom) {
      val prevZoom = currentZoom
      currentZoom = maxZoom
      this.zoomBy(maxZoom / prevZoom)
    } else {
      currentZoom *= zoomFactor
      this.zoomBy(zoomFactor)
    }
  }

  private class ScaleListener(private val onZoom: (Float) -> Unit) : SimpleOnScaleGestureListener() {
    override fun onScale(detector: ScaleGestureDetector): Boolean {
      onZoom(Math.max(0.1f, Math.min(detector.scaleFactor, 100.0f)))
      return true
    }
  }
}