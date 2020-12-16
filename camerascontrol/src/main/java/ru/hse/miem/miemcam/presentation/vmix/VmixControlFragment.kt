package ru.hse.miem.miemcam.presentation.vmix

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.google.android.material.button.MaterialButton
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.fragment_vmix_control.*
import kotlinx.android.synthetic.main.vmix_source_layout.view.*
import ru.hse.miem.miemcam.presentation.main.CamerasActivity
import javax.inject.Inject

class VmixControlFragment : MvpAppCompatFragment(), VmixControlView {

  private val colors = arrayOf(
    Color.parseColor("#E15241"),
    Color.parseColor("#F1A338"),
    Color.parseColor("#67AD5B"),
    Color.parseColor("#4994EC"),
    Color.parseColor("#B8B8B8")
  )
  private val ripples = arrayOf(
    Color.parseColor("#E0ACA6"),
    Color.parseColor("#F2D2A5"),
    Color.parseColor("#B0DBA9"),
    Color.parseColor("#B4CFED"),
    Color.parseColor("#E8E8E8")
  )

  @Inject
  @InjectPresenter
  lateinit var vmixControlPresenter: VmixControlPresenter

  @ProvidePresenter
  fun provideVmixControlPresenter() = vmixControlPresenter

  private var buttons = arrayListOf<ArrayList<MaterialButton>>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_vmix_control, container, false)
  }

  override fun onAttach(context: Context) {
    (activity as CamerasActivity).camerasComponent.inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    vmixControlPresenter.viewCreated()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    buttons = arrayListOf()
  }

  override fun setUpViews() {
    createBtns()
    setUpControlBtns()
    setUpSourceBtns()
  }

  override fun setState(states: List<List<Boolean>>, animated: Boolean) {
    for (line in 0 until states.count()) {
      for (button in 0 until states[line].count()) {
        activity?.runOnUiThread {
          val fromColor = buttons[line][button].currentTextColor
          val toColor = if (states[line][button]) colors[line] else colors[4]
          val fromRipple = buttons[line][button].rippleColor!!.defaultColor
          val toRipple = if (states[line][button]) ripples[line] else ripples[4]
          val duration = if (animated) 300L else 0L
          animateColor(fromColor, toColor, duration) {
            buttons[line][button].setTextColor(it)
            buttons[line][button].strokeColor = ColorStateList.valueOf(it)
          }
          animateColor(fromRipple, toRipple, duration) {
            buttons[line][button].rippleColor = ColorStateList.valueOf(it)
          }
        }
      }
    }
  }

  override fun showError() {
    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
  }

  override fun showErrorIncorrectHost() {
    Toast.makeText(requireContext(), R.string.incorrect_host, Toast.LENGTH_SHORT).show()
  }

  private fun animateColor(
    fromColor: Int,
    toColor: Int,
    duration: Long,
    onAnimate: (value: Int) -> Unit
  ): ValueAnimator {
    val animator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
    animator.duration = duration
    animator.addUpdateListener {
      onAnimate(animator.animatedValue as Int)
    }
    animator.start()
    return animator
  }

  private fun createBtns() {
    for (i in 0..3) {
      val source = LayoutInflater.from(context).inflate(R.layout.vmix_source_layout, null)
      sources.addView(source)
      with (source) {
        sourceName.text = (i + 1).toString()
        sourceName.setTextColor(colors[i])
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val gap = TypedValue.applyDimension(
          TypedValue.COMPLEX_UNIT_DIP,
          8f,
          displayMetrics
        ).toInt()
        layoutParams.width = (width - gap) / 4
        buttons.add(arrayListOf(overlay1, overlay2, overlay3, overlay4, program, preview))
      }
    }
  }

  private fun setUpSourceBtns() {
    for (line in 0 until buttons.count()) {
      for (button in 0 until buttons[line].count()) {
        with(buttons[line][button]) {
          setTextColor(colors[line])
          strokeColor = ColorStateList.valueOf(colors[line])
          rippleColor = ColorStateList.valueOf(ripples[line])
          when (button) {
            4 -> {
              program.setOnClickListener {
                vmixControlPresenter.programBtnClicked(line + 1)
              }
            }
            5 -> {
              preview.setOnClickListener {
                vmixControlPresenter.previewBtnClicked(line + 1)
              }
            }
            else -> {
              setOnClickListener {
                vmixControlPresenter.overlayBtnClicked(button + 1, line + 1)
              }
            }
          }
        }
      }
    }
  }

  private fun setUpControlBtns() {
    cut.setOnClickListener {
      vmixControlPresenter.cutBtnClicked()
    }
    fade.setOnClickListener {
      vmixControlPresenter.fadeBtnClicked()
    }
    recStart.setOnClickListener {
      vmixControlPresenter.recBtnClicked()
    }
    recStop.setOnClickListener {
      vmixControlPresenter.recBtnClicked()
    }
    streamStart.setOnClickListener {
      vmixControlPresenter.streamBtnClicked()
    }
    streamStop.setOnClickListener {
      vmixControlPresenter.streamBtnClicked()
    }
    setHost.setOnClickListener {
      vmixControlPresenter.setVmixPressed(hostField.text.toString())
    }
    resetHost.setOnClickListener {
      hostField.setText("")
      vmixControlPresenter.setVmixPressed()
    }
  }
}
