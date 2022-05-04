package ru.hse.miem.miemapp.presentation.schedule

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.layout_bottom_calendar.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.IScheduleItem
import ru.hse.miem.miemapp.domain.entities.ScheduleDayLesson
import ru.hse.miem.miemapp.domain.entities.ScheduleDayName
import ru.hse.miem.miemapp.presentation.OnBackPressListener
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.profile.ProfileFragmentArgs
import ru.hse.miem.miemapp.presentation.schedule.db.ScheduleDbManager
import java.text.SimpleDateFormat
import javax.inject.Inject

class ScheduleFragment: BaseFragment(R.layout.fragment_schedule), ScheduleView, OnBackPressListener {

    private val args: ProfileFragmentArgs by navArgs()

    private val calendar = CalendarHelper()

    private var defaultDate = calendar.getCurrentDate()
    private var startDate = defaultDate
    private var finishDate = calendar.getNewDate(defaultDate, 6)

    private var defaultDateRu = calendar.getRuFormattedDate(defaultDate)
    private var startDateRu = defaultDateRu
    private var finishDateRu = calendar.getRuFormattedDate(finishDate)

    @Inject
    @InjectPresenter
    lateinit var schedulePresenter: SchedulePresenter

    @ProvidePresenter
    fun provideSchedulePresenter() = schedulePresenter

    private lateinit var calendarBehaviour: BottomSheetBehavior<View>
    private lateinit var dbManager: ScheduleDbManager

    private val scheduleAdapter = ScheduleAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
        dbManager = ScheduleDbManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scheduleList.adapter = scheduleAdapter

        bottomScheduleLoader.visibility = View.GONE

        if(scheduleAdapter.hasData) {
            scheduleLoader.visibility = View.GONE
            scheduleList.visibility = View.VISIBLE
        }

        dateSelector.text = "$defaultDateRu - $finishDateRu"

        calendarBehaviour = BottomSheetBehavior.from(scheduleFilterLayout)
        calendarBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
        calendarBehaviour.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    scheduleLayoutContent.foreground = ColorDrawable(
                        resources.getColor(R.color.transparent)
                    )
                }
            }
        })

        dateSelector.setOnClickListener {
            scheduleLayoutContent.foreground = ColorDrawable(
                resources.getColor(R.color.semi_transparent)
            )
            calendarBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }

        hideCalendarButton.setOnClickListener {
            scheduleLayoutContent.foreground = ColorDrawable(
                resources.getColor(R.color.transparent)
            )
            calendarBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        scheduleCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->

            bottomScheduleLoader.visibility = View.VISIBLE

            val selectedDate = "$dayOfMonth/${month+1}/$year"

            scheduleCalendar.date = SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).time

            startDate = calendar.getApiFormattedDate(year, month + 1, dayOfMonth)
            finishDate = calendar.getNewDate(startDate, 6)

            startDateRu = calendar.getRuFormattedDate(startDate)
            finishDateRu = calendar.getRuFormattedDate(finishDate)

            dateSelector.text = "$startDateRu - $finishDateRu"

            schedulePresenter.onNewDateSelected(startDate, finishDate, args.isTeacher)

            scheduleList.smoothSnapToPosition(0)

            scheduleLayoutContent.foreground = ColorDrawable(resources.getColor(R.color.transparent))
            calendarBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        
        scheduleList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(!scheduleList.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE &&
                    bottomScheduleLoader.visibility != View.VISIBLE) {

                    bottomScheduleLoader.visibility = View.VISIBLE

                    schedulePresenter.onScrolledDown(
                        startDate = calendar.getNewDate(finishDate, 1),
                        finishDate = calendar.getNewDate(finishDate, 7),
                        isTeacher = args.isTeacher
                    )

                    finishDate = calendar.getNewDate(finishDate, 6)
                    finishDateRu = calendar.getRuFormattedDate(finishDate)
                    dateSelector.text = "$startDateRu - $finishDateRu"
                }
            }
        })

        schedulePresenter.loadFromDb(dbManager)
        scheduleLoader.visibility = View.GONE
        bottomScheduleLoader.visibility = View.VISIBLE
    }

    override fun loadSchedule(items: List<IScheduleItem>) {
        if (items.isNotEmpty()) {
            setupSchedule(items)
        }
        schedulePresenter.onCreate(startDate, finishDate, args.isTeacher)
    }

    private fun saveToDb(items: List<IScheduleItem>) {
        dbManager.openDb()
        dbManager.deleteDb()
        dbManager.openDb()

        for (i in items.indices) {
            val item = items[i]

            if(item is ScheduleDayName) {
                dbManager.insertDb(
                    type = "day",
                    date = item.date,
                    dayOfWeek = item.dayOfWeek
                )
            }
            else if (item is ScheduleDayLesson) {
                dbManager.insertDb(
                    type = "lesson",
                    date = item.date,
                    dayOfWeek = item.dayOfWeek,
                    auditorium = item.lesson.auditorium,
                    beginLesson = item.lesson.beginLesson,
                    endLesson = item.lesson.endLesson,
                    lessonNumber = item.lesson.lessonNumberStart,
                    address = item.lesson.building,
                    discipline = item.lesson.discipline,
                    kindOfLesson = item.lesson.kindOfWork,
                    lecturer = item.lesson.lecturer
                )
            }
        }
        dbManager.closeDb()
    }

    override fun onBackPressed(): Boolean {
        if (calendarBehaviour.state != BottomSheetBehavior.STATE_COLLAPSED) {
            scheduleLayoutContent.foreground = ColorDrawable(resources.getColor(R.color.transparent))
            calendarBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false
    }

    override fun updateScheduleWhenScrolledDown(newItems: List<IScheduleItem>) {
        (scheduleList.adapter as ScheduleAdapter?)?.updateWhenScrolledDown(newItems)
        bottomScheduleLoader.visibility = View.GONE
    }

    override fun updateScheduleWhenNewDateSelected(newItems: List<IScheduleItem>) {
        (scheduleList.adapter as ScheduleAdapter?)?.update(newItems)
        bottomScheduleLoader.visibility = View.GONE
    }

    override fun setupSchedule(items: List<IScheduleItem>) {
        if (items.isEmpty()) {
            finishDate = calendar.getNewDate(finishDate, 7)
            finishDateRu = calendar.getRuFormattedDate(finishDate)

            schedulePresenter.onCreate(startDate, finishDate, args.isTeacher)
            dateSelector.text = "$startDateRu - $finishDateRu"
        } else {
            scheduleAdapter.update(items)

            scheduleLoader.visibility = View.GONE
            bottomScheduleLoader.visibility = View.GONE
            scheduleList.visibility = View.VISIBLE
            saveToDb(items)
        }

    }

    private fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun getHorizontalSnapPreference(): Int = snapMode
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }
}