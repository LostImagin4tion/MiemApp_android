package ru.hse.miem.miemapp.presentation.tinder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_tinder.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import java.util.*
import javax.inject.Inject

class TinderFragment : BaseFragment(R.layout.fragment_tinder), InfoView{

    private lateinit var projectVacancies: List<ProjectExtended.Vacancy>

    private lateinit var adapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager

    lateinit var items: ArrayList<ItemModel>

    companion object {
        fun newInstance() = TinderFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: TinderPresenter

    @ProvidePresenter
    fun provideSearchPresenter() = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        items = arrayListOf()
        presenter.onCreate()
        Log.d("AddsMyLogs", "2")
    }

    override fun setupProjects(projects: List<ProjectInSearch>) {
        if (projects.isEmpty()){
            Log.d("MyLogs", "Empty")
        }else{
            Log.d("MyLogs", "NotEmpty")
        }

//        items = arrayListOf()

        for (i in projects.indices) {

            if (projects[i].vacancies > 0){

                Log.d("MyLogs", "Yes")

                Log.d("tinder", i.toString() + " " + projects[i].id)

                presenter.infoProject(projects[i].id)
            }else{
                Log.d("MyLogs", "No")
            }
        }

        Log.d("AddsMyLogs", "1")

    }

    override fun setupProject(project: ProjectExtended){
        for (i in project.vacancies.indices) {
            if (project.vacancies[i].role.isEmpty()){
                Log.d("InfoMyLogs", "(((")
            }
            items.add(
                ItemModel(
                    R.drawable.sample1,
                    project.type,
                    project.name,
                    project.vacancies[i].role,
                    "",
                    ""
                )
            )
            Log.d("InfoMyLogs", i.toString() + " " + project.vacancies[i].role)
        }
        Log.d("AddsMyLogs", "0")
        smt()
    }

    fun smt(){

        adapter = CardStackAdapter(items)
        val cardStackView: CardStackView = card_stack_view
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator ()
    }

    override fun showError() {

    }

}