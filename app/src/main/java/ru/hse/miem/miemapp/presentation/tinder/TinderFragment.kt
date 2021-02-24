package ru.hse.miem.miemapp.presentation.tinder

import ru.hse.miem.miemapp.data.repositories.ProjectRepository


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.fragment_tinder.*
import kotlinx.android.synthetic.main.layout_bottom_filters.*
import moxy.MvpPresenter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.project.*
import ru.hse.miem.miemapp.presentation.search.ProjectsAdapter
import ru.hse.miem.miemapp.presentation.search.SearchFilters
import ru.hse.miem.miemapp.presentation.search.SearchView
import java.util.*
import javax.inject.Inject

class TinderFragment : BaseFragment(R.layout.fragment_tinder), SearchView, ProjectView {

    private lateinit var projectVacancies: List<ProjectExtended.Vacancy>

    private lateinit var adapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager

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
        presenter.onCreate()
    }

    override fun setupProjects(projects: List<ProjectInSearch>) {
        if (projects.isEmpty()){
            Log.d("TinderMyLogs", "Empty")
        }else{
            Log.d("TinderMyLogs", "NotEmpty")
        }

        val items: ArrayList<ItemModel> = arrayListOf()

        for (i in projects.indices) {

            if (projects[i].vacancies > 0){

                Log.d("TinderMyLogs", "Yes")

                lateinit var projectPresenter: ProjectPresenter

                projectPresenter.onCreate(617)

//                Log.d("TinderMyLogs", projectVacancies.size.toString())

                items.add(
                    ItemModel(
                        R.drawable.sample1,
                        projects[i].type,
                        projects[i].name,
                        "Разработчик мобильного приложения (Android)",
                        "Android, Java, Kotlin",
                        "Королев Денис"
                    )
                )
            }else{
                Log.d("TinderMyLogs", "No")
            }
        }

        adapter = CardStackAdapter(items)

        val cardStackView: CardStackView = card_stack_view
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator ()

    }

    override fun setupProject(project: ProjectExtended) = project.run {
//        projectType.text = getString(R.string.project_type_and_number).format(number, type, source)
//        projectName.text = name
//        projectState.text = state
//        projectState.setBackgroundResource(if (isActive) R.drawable.project_badge_active_bg else R.drawable.project_badge_inactive_bg)
//        projectEmail.text = email
        Log.d("TinderMyLogs", "HUI")
        projectVacancies = project.vacancies
    }

}