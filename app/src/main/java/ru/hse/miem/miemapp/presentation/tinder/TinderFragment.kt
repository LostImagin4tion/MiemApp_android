package ru.hse.miem.miemapp.presentation.tinder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_tinder.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.tinder.db.DbManager
import java.util.*
import javax.inject.Inject


class TinderFragment : BaseFragment(R.layout.fragment_tinder), InfoView{
    private var adapter: CardStackAdapter = CardStackAdapter()
    private var items: ArrayList<VacancyCard> = arrayListOf()
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

        if (adapter.hadData){
            tinderLoader.visibility = View.GONE
        }

        viewLiked.setOnClickListener {
            findNavController().navigate(R.id.fragmentVacancies)
        }

        presenter.onCreate()
    }

    override fun setupVacancies(projects: List<Vacancies>) {
        if (items.size == 0) {
            items.add(
                VacancyCard(
                    "",
                    getResources().getString(R.string.tinder_welcome),
                    "",
                    ""
                )
            )
            items.add(
                VacancyCard(
                    "",
                    "На данный момент Вы просмотрели\n" +
                            "все доступные вакансии",
                    "",
                    ""
                )
            )
        }

        for (item in projects){
            items.add(items.size - 1,
                VacancyCard(
                    "#" + item.project_id,
                    item.project_name_rus,
                    item.vacancy_role,
                    item.vacancy_disciplines.toString() + item.vacancy_additionally
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
        items = Sorting.sort(items)
        adapter = CardStackAdapter(items)
        val cardStackView: CardStackView = card_stack_view
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun load(){
        dbManager.openDb()

        for (item in dbManager.readDb()){
            Sorting.likeVacancies.add(item)
            Sorting.addRole(item.vacancy_role)
            Sorting.addCategory(item.requirements)
        }

        dbManager.closeDb()
    }

    private fun save(){
        dbManager.openDb()
        for (i in Sorting.likeIndexes) {
            if (items[i].project_id != ""){
                Sorting.likeVacancies.add(items[i])
                dbManager.insertDb(items[i].project_id, items[i].project_name_rus, items[i].vacancy_role, items[i].requirements)
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