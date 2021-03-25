package ru.hse.miem.miemapp.presentation.vacancies

import android.content.Context
import android.os.Bundle
import android.view.View
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import javax.inject.Inject

class VacanciesFragment : BaseFragment(R.layout.fragment_vacancies), ViewAllView {
    @Inject
    @InjectPresenter
    lateinit var vacanciesPresenter: VacanciesPresenter

    @ProvidePresenter
    fun provideViewAllPresenter() = vacanciesPresenter

    // TO-DO
//    private val args: TinderFragmentArgs by navArgs()

//    private val viewAllAdapter = ViewAllAdapter {
//        val action = SearchFragmentDirections.actionFragmentSearchToFragmentProject(it)
//        findNavController().navigate(action)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        projectsList.adapter = viewAllAdapter
//        if (viewAllAdapter.hasData) {
//            viewAllLoader.visibility = View.GONE
//            projectsList.visibility = View.VISIBLE
//        }

        vacanciesPresenter.onCreate()
    }

    override fun setupLovedVacancies(vacancies: List<Vacancies>) {
        var trueVacancies : List<Vacancies> = emptyList()
//        val lovedVacancies = args
//        for (vacancy in vacancies)
//            if vacancy.project_id =
        // to-do

//        ViewAllAdapter.update(trueVacancies)

//
//        viewAllLoader.visibility = View.GONE
//        projectsList.visibility = View.VISIBLE
    }

    override fun showError() {}
}