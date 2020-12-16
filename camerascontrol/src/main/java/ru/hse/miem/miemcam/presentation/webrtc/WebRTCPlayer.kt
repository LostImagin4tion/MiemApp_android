package ru.hse.miem.miemcam.presentation.webrtc

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.webrtc_player.*

class WebRTCPlayer: Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.webrtc_player, container, false)
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    webRtcDisplay.settings.javaScriptEnabled = true
    webRtcDisplay.settings.javaScriptCanOpenWindowsAutomatically = true
    webRtcDisplay.settings.mediaPlaybackRequiresUserGesture = false
    webRtcDisplay.webChromeClient =
      WebChromeClientCustomPoster()
    webRtcDisplay.webViewClient = WebViewClient()
    webRtcDisplay.addJavascriptInterface(
      JSInterface(
        activity,
        webRtcDisplay,
        webRtcLoadingProgress
      ),
      "Android"
    )
    webRtcDisplay.loadUrl("file:///android_asset/index.html")
  }

  fun openVideoUrl(url: String) {
    webRtcDisplay.loadUrl("javascript:start('$url')")
  }

  fun stopVideo() {
    webRtcDisplay.loadUrl("javascript:stop()")
    webRtcDisplay.visibility = View.INVISIBLE
    webRtcLoadingProgress.visibility = View.INVISIBLE
  }
}

class JSInterface(
  val activity: Activity?,
  val webRtcDisplay: WebRTCDisplay,
  val webRtcLoadingProgress: ProgressBar
) {

  @JavascriptInterface
  fun setLoading(isLoading: String) {
    if (isLoading == "true") {
      activity?.runOnUiThread {
        webRtcLoadingProgress.visibility = View.VISIBLE
        webRtcDisplay.visibility = View.INVISIBLE
      }
    } else {
      activity?.runOnUiThread {
        webRtcDisplay.resetZoom()
        webRtcLoadingProgress.visibility = View.INVISIBLE
        webRtcDisplay.visibility = View.VISIBLE
      }
    }
  }

  @JavascriptInterface
  fun log(lvl: String, log: String) {
    when (lvl) {
      "i" -> Log.i("WebRTCPlayer", log)
      "e" -> Log.e("WebRTCPlayer", log)
    }
  }

  @JavascriptInterface
  fun setMaxZoom(maxZoom: String) {
    webRtcDisplay.maxZoom = maxZoom.toFloat()
  }
}

private class WebChromeClientCustomPoster : WebChromeClient() {
  override fun getDefaultVideoPoster(): Bitmap {
    return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
  }

  override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
    Log.d("WebView", consoleMessage.message())
    return true
  }
}