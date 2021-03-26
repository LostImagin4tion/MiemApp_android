package ru.hse.miem.miemapp.presentation.vacancies

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_vacancies.*
import kotlinx.android.synthetic.main.layout_bottom_filters.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.tinder.Sorting
import ru.hse.miem.miemapp.presentation.tinder.db.DbManager
import java.util.ArrayList
import javax.inject.Inject

class VacanciesFragment : BaseFragment(R.layout.fragment_vacancies), ViewAllView {
    @Inject
    @InjectPresenter
    lateinit var vacanciesPresenter: VacanciesPresenter
    private lateinit var dbManager: DbManager

    @ProvidePresenter
    fun provideVacanciesPresenter() = vacanciesPresenter

    private val vacanciesAdapter = VacanciesAdapter {
        val action = VacanciesFragmentDirections.actionFragmentVacanciesToFragmentProject(it)
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
        dbManager = DbManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vacancyList.adapter = vacanciesAdapter
        if (vacanciesAdapter.hasData) {
            viewAllLoader.visibility = View.GONE
            vacancyList.visibility = View.VISIBLE
        }
        vacanciesPresenter.onCreate()
    }

    override fun setupLovedVacancies(vacancies: List<Vacancies>) {
        vacanciesAdapter.update(Sorting.likeVacancies.toList())
        viewAllLoader.visibility = View.GONE
        vacancyList.visibility = View.VISIBLE
    }

    override fun showError() {}
}