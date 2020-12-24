package ru.hse.miem.miemcam.presentation.control

import android.os.Bundle
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.fragment_control_panel.*
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.DisplayMetrics
import android.widget.SeekBar
import ru.hse.miem.miemcam.domain.repositories.FocusMode
import ru.hse.miem.miemcam.domain.repositories.SettingType
import ru.hse.miem.miemcam.presentation.views.SectionView.OnScrollStoppedListener
import android.net.Uri
import android.view.*
import android.widget.Toast
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemcam.presentation.main.CamerasActivity
import ru.hse.miem.miemcam.presentation.views.TouchPadView
import ru.hse.miem.miemcam.presentation.webrtc.WebRTCPlayer
import javax.inject.Inject

// FIXME DO NOT PASS ARGUMENTS TO FRAGMENT VIA CONSTRUCTOR
class ControlPanelFragment(
  private val resetTitle: () -> Unit
) : MvpAppCompatFragment(), ControlPanelView {

  @Inject
  @InjectPresenter
  lateinit var controlPanelPresenter: ControlPanelPresenter

  @ProvidePresenter
  fun provideControlPanelPresenter() = controlPanelPresenter

  private var webRTCPlayer = WebRTCPlayer()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_control_panel, container, false)
  }

  override fun onAttach(context: Context) {
    (activity as CamerasActivity).camerasComponent.inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    touchPad.addView(TouchPadView(context))
    setUpWebRTCPlayer()
    setUpActions()
  }

  override fun onStop() {
    super.onStop()
    controlPanelPresenter.viewDestroyed()
  }

  override fun changeIsEnabledFocusAutoBtn(isEnabled: Boolean) {
    auto.isEnabled = !isEnabled
    activity?.runOnUiThread {
      if (isEnabled) {
        auto.setTextColor(Color.BLACK)
        auto.strokeColor = ColorStateList.valueOf(Color.BLACK)
      } else {
        auto.setTextColor(Color.GRAY)
        auto.strokeColor = ColorStateList.valueOf(Color.GRAY)
      }
    }
  }

  override fun changeIsEnabledFocusManualBtn(isEnabled: Boolean) {
    manual.isEnabled = !isEnabled
    activity?.runOnUiThread {
      if (isEnabled) {
        manual.setTextColor(Color.BLACK)
        manual.strokeColor = ColorStateList.valueOf(Color.BLACK)
      } else {
        manual.setTextColor(Color.GRAY)
        manual.strokeColor = ColorStateList.valueOf(Color.GRAY)
      }
    }
  }

  override fun startStream(uri: String) {
    webRTCPlayer.openVideoUrl(uri)
  }

  override fun releaseCamera() {
    resetTitle()
    resetView()
  }

  override fun showError() {
    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
  }

  fun resetView() {
      auto.setTextColor(Color.GRAY)
      auto.strokeColor = ColorStateList.valueOf(Color.GRAY)
      manual.setTextColor(Color.GRAY)
      manual.strokeColor = ColorStateList.valueOf(Color.GRAY)
      webRTCPlayer.stopVideo()
      controlPanelPresenter.startStream()
  }

  private fun setUpWebRTCPlayer() {
    childFragmentManager.beginTransaction()
      .replace(R.id.webRtcPlayerHolder, webRTCPlayer)
      .commit()
  }

  private fun setUpActions() {
    setUpPresetBtns()
    setUpZoomBtns()
    setUpTouchPad()
    setUpScrollView()
    setUpQuickSettings()
    setUpFocusBtns()
    setUpFocusModeBtns()
  }

  private fun setUpPresetBtns() {
    preset1.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(1)
    }
    preset2.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(2)
    }
    preset3.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(3)
    }
    preset4.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(4)
    }
    preset5.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(5)
    }
    preset6.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(6)
    }
    preset7.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(7)
    }
    preset8.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(8)
    }
    preset9.setOnClickListener {
      controlPanelPresenter.presetBtnClicked(9)
    }
    preset1.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(1)
      true
    }
    preset2.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(2)
      true
    }
    preset3.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(3)
      true
    }
    preset4.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(4)
      true
    }
    preset5.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(5)
      true
    }
    preset6.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(6)
      true
    }
    preset7.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(7)
      true
    }
    preset8.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(8)
      true
    }
    preset9.setOnLongClickListener {
      controlPanelPresenter.presetBtnLongClicked(9)
      true
    }
  }

  private fun setUpZoomBtns() {
    zoomIn.setOnTouchListener { view, motionEvent ->
      if (motionEvent.action == MotionEvent.ACTION_DOWN) {
        view.isPressed = true
        controlPanelPresenter.zoomBtnClicked(true)
      } else if (motionEvent.action == MotionEvent.ACTION_UP) {
        view.isPressed = false
        controlPanelPresenter.stopped()
      }
      view.performClick()
      true
    }
    zoomOut.setOnTouchListener { view, motionEvent ->
      if (motionEvent.action == MotionEvent.ACTION_DOWN) {
        view.isPressed = true
        controlPanelPresenter.zoomBtnClicked(false)
      } else if (motionEvent.action == MotionEvent.ACTION_UP) {
        view.isPressed = false
        controlPanelPresenter.stopped()
      }
      view.performClick()
      true
    }
  }

  private fun setUpTouchPad() {
    touchPad.setOnTouchListener { view, motionEvent ->
      if (motionEvent.action == MotionEvent.ACTION_MOVE || motionEvent.action == MotionEvent.ACTION_DOWN) {
        val x = motionEvent.x / (view.width / 2) - 1
        val y = 1 - motionEvent.y / (view.height / 2)
        controlPanelPresenter.touchPadTouched(x, y)
      } else if (motionEvent.action == MotionEvent.ACTION_UP) {
        controlPanelPresenter.stopped()
      }
      view.performClick()
      true
    }
  }

  private fun setUpScrollView() {
    val displayMetrics = DisplayMetrics()
    (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    controlsPlaceholder.layoutParams.width = 2 * width
    tableLayout.layoutParams.width = width
    quickSettingsLayout.layoutParams.width = width

    scrollView.setOnTouchListener { view, motionEvent ->
      if (motionEvent.action == MotionEvent.ACTION_UP) {
        scrollView.startScrollerTask()
      }
      view.performClick()
      false
    }

    scrollView.setOnScrollStoppedListener(object : OnScrollStoppedListener {
      override fun onScrollStopped() {
        if (scrollView.scrollX > width / 2) {
          scrollView.smoothScrollTo(width, 0)
        } else {
          scrollView.smoothScrollTo(0, 0)
        }
      }
    })
  }

  private fun setUpQuickSettings() {
    brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onStartTrackingTouch(p0: SeekBar?) { }

      override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }

      override fun onStopTrackingTouch(seekBar: SeekBar) {
        controlPanelPresenter.setSetting(SettingType.BRIGHTNESS, brightness.progress.toFloat())
      }
    })

    contrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onStartTrackingTouch(p0: SeekBar?) { }

      override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }

      override fun onStopTrackingTouch(seekBar: SeekBar) {
        controlPanelPresenter.setSetting(SettingType.CONTRAST, contrast.progress.toFloat())
      }
    })

    saturation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onStartTrackingTouch(p0: SeekBar?) { }

      override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }

      override fun onStopTrackingTouch(seekBar: SeekBar) {
        controlPanelPresenter.setSetting(SettingType.SATURATION, saturation.progress.toFloat())
      }
    })
  }

  private fun setUpFocusBtns() {
    focusIn.setOnTouchListener { view, motionEvent ->
      if (motionEvent.action == MotionEvent.ACTION_DOWN) {
        view.isPressed = true
        controlPanelPresenter.focusBtnClicked(true)
      } else if (motionEvent.action == MotionEvent.ACTION_UP) {
        view.isPressed = false
        controlPanelPresenter.focusStopped()
      }
      view.performClick()
      true
    }
    focusOut.setOnTouchListener { view, motionEvent ->
      if (motionEvent.action == MotionEvent.ACTION_DOWN) {
        view.isPressed = true
        controlPanelPresenter.focusBtnClicked(false)
      } else if (motionEvent.action == MotionEvent.ACTION_UP) {
        view.isPressed = false
        controlPanelPresenter.focusStopped()
      }
      view.performClick()
      true
    }
  }

  private fun setUpFocusModeBtns() {
    auto.setOnClickListener {
      controlPanelPresenter.setFocusMode(FocusMode.AUTO)
    }
    manual.setOnClickListener {
      controlPanelPresenter.setFocusMode(FocusMode.MANUAL)
    }
  }
}
