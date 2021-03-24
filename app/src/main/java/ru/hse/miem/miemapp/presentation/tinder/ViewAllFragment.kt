package ru.hse.miem.miemapp.presentation.tinder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.projectsList
import kotlinx.android.synthetic.main.fragment_tinder.*
import kotlinx.android.synthetic.main.fragment_viewall.*
import kotlinx.android.synthetic.main.item_project_in_search.view.*
import kotlinx.android.synthetic.main.layout_bottom_filters.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.TextViewUtils.makeNameValueString
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.project.ProjectFragmentArgs
import ru.hse.miem.miemapp.presentation.search.*
import ru.hse.miem.miemapp.presentation.tinder.db.DbManager
import java.util.*
import javax.inject.Inject

class ViewAllFragment : BaseFragment(R.layout.fragment_viewall), ViewAllView {
    @Inject
    @InjectPresenter
    lateinit var viewAllPresenter: ViewAllPresenter

    @ProvidePresenter
    fun provideViewAllPresenter() = viewAllPresenter

    // TO-DO
    private val args: TinderFragmentArgs by navArgs()

    private val viewAllAdapter = ViewAllAdapter {
        val action = SearchFragmentDirections.actionFragmentSearchToFragmentProject(it)
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectsList.adapter = viewAllAdapter
        if (viewAllAdapter.hasData) {
            viewAllLoader.visibility = View.GONE
            projectsList.visibility = View.VISIBLE
        }

        viewAllPresenter.onCreate()
    }

    override fun setupLovedVacancies(vacancies: List<Vacancies>) {
        var trueVacancies : List<Vacancies> = emptyList()
        val lovedVacancies = args
        for (vacancy in vacancies)
            if vacancy.project_id =
    // to-do

        ViewAllAdapter.update(trueVacancies)


        viewAllLoader.visibility = View.GONE
        projectsList.visibility = View.VISIBLE
    }

    override fun showError() {}
}



