package ru.hse.miem.miemapp.presentation.tinder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tinder.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.search.SearchFragment
import ru.hse.miem.miemapp.presentation.tinder.db.DbManager
import java.util.*
import javax.inject.Inject


class TinderFragment : BaseFragment(R.layout.fragment_tinder), InfoView {
    private var items: ArrayList<VacancyCard> = arrayListOf()
    private var adapter: CardStackAdapter = CardStackAdapter(items)
    private val listener = CardStackCallback()
    private lateinit var manager: CardStackLayoutManager
    private lateinit var dbManager: DbManager

    @Inject
    @InjectPresenter
    lateinit var presenter: TinderPresenter

    @ProvidePresenter
    fun provideTinderPresenter() = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
        dbManager = DbManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        load()

        if (adapter.hadData) {
            tinderLoader.visibility = View.GONE
        }

        viewLiked.setOnClickListener {
            findNavController().navigate(R.id.fragmentVacancies)
        }

        reset.setOnClickListener {
            Sorting.reset()
            dbManager.deleteDb()
            findNavController().popBackStack(R.id.fragmentTinder, true)
            findNavController().navigate(R.id.fragmentTinder)
        }
        presenter.onCreate()
    }

    override fun setupVacancies(projects: List<Vacancies>) {
        if (items.size == 0) {
            items.add(
                VacancyCard(
                    "",
                    resources.getString(R.string.tinder_welcome),
                    "",
                    ""
                )
            )
            items.add(
                VacancyCard(
                    "",
                    resources.getString(R.string.tinder_end_1),
                    "",
                    ""
                )
            )
        }

        projects.forEach {
            items.add(
                items.size - 1,
                VacancyCard(
                    "#" + it.projectId,
                    it.projectNameRus,
                    it.vacancyRole,
                    it.vacancyDisciplines.toString() + it.vacancyAdditionally
                )
            )
        }
        filling()
    }

    private fun filling() {
        manager = CardStackLayoutManager(this.requireContext(), listener)
        manager.setStackFrom(StackFrom.None)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        items = Sorting.sort(items)
        adapter = CardStackAdapter(items)
        val cardStackView: CardStackView = card_stack_view
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
        tinderEnd.visibility = View.VISIBLE
        tinderLoader.visibility = View.INVISIBLE
    }

    private fun load() {
        dbManager.openDb()

        for (item in dbManager.readDb()) {
            Sorting.likeVacancies.add(item)
            Sorting.addRole(item.vacancyRole)
            Sorting.addCategory(item.requirements)
        }

        dbManager.closeDb()
    }

    private fun save() {
        dbManager.openDb()
        for (i in Sorting.likeIndexes) {
            if (items[i].projectId != "") {
                Sorting.likeVacancies.add(items[i])
                dbManager.insertDb(
                    items[i].projectId,
                    items[i].projectNameRus,
                    items[i].vacancyRole,
                    items[i].requirements
                )
            }
        }
        dbManager.closeDb()
    }

    override fun onStop() {
        super.onStop()
        Sorting.position = listener.pos
        save()
        Sorting.clear()
    }
}