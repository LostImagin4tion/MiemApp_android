package ru.hse.miem.miemapp.presentation.tinder

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_tinder.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.tinder.db.DbManager
import java.io.File
import java.util.*
import javax.inject.Inject


class TinderFragment : BaseFragment(R.layout.fragment_tinder), InfoView{
    private var adapter: CardStackAdapter = CardStackAdapter()
    private var items: ArrayList<ItemModel> = arrayListOf()
    private val listener = CardStackCallback()
    private lateinit var manager: CardStackLayoutManager
    private val sorting = Sorting()
    private lateinit var dbManager: DbManager

    @Inject
    @InjectPresenter
    lateinit var presenter: TinderPresenter

    @ProvidePresenter
    fun provideSearchPresenter() = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
        dbManager = DbManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sorting.clear()
        load()

        Log.d("TinderMyLogs", "ViewCreated")

        Log.d("tinderLogs", sorting.getRoles().toString())

        if (adapter.hadData){
            tinderLoader.visibility = View.GONE
        }

        viewAll.setOnClickListener {
            Log.d("LogTinderAct", listener.likeVacancy.size.toString())
        }

        presenter.onCreate()
    }

    override fun setupVacancies(projects: List<Vacancies>) {
        if (items.size == 0) {
            items.add(
                ItemModel(
                    R.drawable.sample2,
                    "",
                    "Добро пожаловать в проектный тиндер\n\nСвайп вправо, если понравилась вакансия",
                    "Свайп влево - нет",
                    "",
                    ""
                )
            )
            items.add(
                ItemModel(
                    R.drawable.sample2,
                    "",
                    "",
                    "",
                    "",
                    ""
                )
            )
        }

        for (item in projects){
            items.add(items.size - 1,
                ItemModel(
                    R.drawable.sample1,
                    "#" + item.project_id,
                    item.project_name_rus,
                    item.vacancy_role,
                    item.vacancy_disciplines.toString() + item.vacancy_additionally,
                    ""
                )
            )
        }
        filling()
    }

    private fun filling(){
        manager = CardStackLayoutManager(this.requireContext(), listener)
        manager.setStackFrom(StackFrom.None)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(items.drop(Sorting.count))
        val cardStackView: CardStackView = card_stack_view
        cardStackView.layoutManager = manager;
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator ()
    }

    override fun onStop() {
        super.onStop()
        Log.d("tinder", "Stop")
        Sorting.position = listener.pos
        Log.d("tinderLogs", Sorting.roles.toString())
        save()
        sorting.clear()
    }

    private fun load(){
        sorting.clear()
        dbManager.openDb()
        for (item in dbManager.readDb()){
            sorting.add(item.key, item.value)
        }
        dbManager.closeDb()
    }

    private fun save(){
        dbManager.openDb()
        for (item in listener.likeVacancy){
            if (item < items.size) {
                sorting.add(items[item].vacancy)
            }else{
                break
            }
        }
        for (item in Sorting.roles){
            dbManager.insertDb(item.key, item.value)
        }
        dbManager.closeDb()
        sorting.clear()
    }

    override fun showError() {}
}