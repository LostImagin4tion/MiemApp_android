package ru.hse.miem.miemcam.presentation.main

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import ru.hse.miem.miemcam.presentation.util.FragmentType
import ru.hse.miem.miemcam.R
import kotlinx.android.synthetic.main.activity_cameras.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemcam.dagger.CamerasControlComponent
import ru.hse.miem.miemcam.dagger.DaggerCamerasControlComponent
import javax.inject.Inject


class CamerasActivity : MvpAppCompatActivity(), CamerasView {

    lateinit var camerasComponent: CamerasControlComponent

    @Inject
    @InjectPresenter
    lateinit var camerasPresenter: CamerasPresenter

    @ProvidePresenter
    fun provideCamerasPresenter() = camerasPresenter

    private var animator: ValueAnimator? = null

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_record -> {
                    camerasPresenter.onFragmentPicked(FragmentType.RECORD)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_more -> {
                    camerasPresenter.onMorePressed()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        camerasComponent = DaggerCamerasControlComponent.builder()
            .appComponent((application as MiemApplication).appComponent)
            .build()

        camerasComponent.inject(this)
        setTheme(R.style.CamerasControlTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cameras)
        toolbar.visibility = View.INVISIBLE
        navigation.visibility = View.INVISIBLE

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        camerasPresenter.onStart()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is TextInputEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun setUpViews() {
        toolbar.visibility = View.VISIBLE
        navigation.visibility = View.VISIBLE
        morePlaceholder.visibility = View.INVISIBLE
        camerasListPlaceholder.visibility = View.INVISIBLE
        setUpActionBar()
    }

    override fun setUpMenu(menuItemAdapter: MenuItemAdapter) {
        val viewManager = GridLayoutManager(this, 3)
        menuGrid.layoutManager = viewManager
        menuGrid.adapter = menuItemAdapter
        menuGrid.alpha = 0f
    }

    override fun setFragment(f: Fragment) {
        setFragment(f, R.id.placeholder)
    }

    override fun setBackgroundFragment(f: Fragment) {
        setFragment(f, R.id.camerasListPlaceholder)
    }

    override fun openCameraPicker() {
        runOnUiThread {
            placeholder.animate()
                .translationY(placeholder.height.toFloat())
                .withStartAction {
                    toolbar.elevation = 4f
                    camerasListPlaceholder.visibility = View.VISIBLE
                }
            arrowIcon.animate()
                .rotation(180f)
        }
    }

    override fun closeCameraPicker() {
        runOnUiThread {
            placeholder.animate()
                .translationY(0f)
                .withEndAction {
                    toolbar.elevation = 0f
                    camerasListPlaceholder.visibility = View.INVISIBLE
                }
            arrowIcon.animate()
                .rotation(0f)
        }
    }

    override fun setActionBarLabel(text: String) {
        runOnUiThread {
            toolbarTitle.text = text
        }
    }

    override fun setArrowVisibility(isVisible: Boolean) {
        if (isVisible) {
            arrowIcon.visibility = View.VISIBLE
        } else {
            arrowIcon.visibility = View.INVISIBLE
        }
    }

    override fun setLoadingVisibility(isVisible: Boolean) {
        runOnUiThread {
            if (isVisible) {
                loadingProgressCamera.visibility = View.VISIBLE
            } else {
                loadingProgressCamera.visibility = View.INVISIBLE
            }
        }
    }

    override fun setMoreVisibility(isVisible: Boolean) {
        if (isVisible) {
            morePlaceholder.visibility = View.VISIBLE
            animateMenuScale(0f, 1f) {}
        } else {
            animateMenuScale(1f, 0f) {
                morePlaceholder.visibility = View.INVISIBLE
                menuGrid.scrollToPosition(0)
            }
        }
    }

    private fun animateMenuScale(from: Float, to: Float, completion: () -> Unit = {}) {
        animator?.cancel()
        val last: Float = if (animator != null) animator!!.animatedValue as Float else from
        animator = ValueAnimator.ofFloat(last, to)
        menuPlaceholder.pivotX = menuPlaceholder.width.toFloat()
        menuPlaceholder.pivotY = menuPlaceholder.height.toFloat()
        menuGrid.pivotX = menuGrid.width.toFloat()
        menuGrid.pivotY = menuGrid.height.toFloat()
        animator?.addUpdateListener {
            val value = animator?.animatedValue as Float
            menuPlaceholder.scaleX = value
            menuPlaceholder.scaleY = value
            val threshold = 0.2f
            if (value >= threshold) {
                menuGrid.scaleX = 1f / value
                menuGrid.scaleY = 1f / value
                menuGrid.alpha = (value - threshold) / (1 - threshold)
            }
        }
        animator?.doOnEnd {
            if (animator?.animatedValue == to) {
                completion()
            }
        }
        animator?.start()
    }

    override fun updateMenuItems() {
        menuGrid.adapter?.notifyDataSetChanged()
    }

    override fun selectMoreNavigation() {
        navigation.menu.findItem(R.id.navigation_more).isChecked = true
    }

    override fun showError() {
        Toast.makeText(this, R.string.no_access_error, Toast.LENGTH_SHORT).show()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0F
        toolbarBtn.setOnClickListener {
            camerasPresenter.changeCamerasListVisibility()
        }
    }

    private fun setFragment(f: Fragment, placeholder: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(placeholder, f)
            commit()
        }
    }

}
