package ru.hse.miem.miemapp.presentation.vacancies

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_vacancies.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.tinder.Sorting
import java.util.ArrayList
import javax.inject.Inject

class VacanciesFragment : BaseFragment(R.layout.fragment_vacancies), VacanciesView {
    @Inject
    @InjectPresenter
    lateinit var vacanciesPresenter: VacanciesPresenter

    @ProvidePresenter
    fun provideVacanciesPresenter() = vacanciesPresenter

    private val vacanciesAdapter = VacanciesAdapter {
        val action = VacanciesFragmentDirections.actionFragmentVacanciesToFragmentProject(it)
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vacancyList.adapter = vacanciesAdapter
        if (vacanciesAdapter.hasData) {
            vacanciesLoader.visibility = View.GONE
            vacancyList.visibility = View.VISIBLE
        }
        vacanciesPresenter.onCreate()
    }

    override fun setupLovedVacancies(vacancies: List<Vacancies>) {
        val items: ArrayList<VacancyCard> = arrayListOf()
        for (item in vacancies) {
            items.add(
                VacancyCard(
                    "#" + item.projectId,
                    item.projectNameRus,
                    item.vacancyRole,
                    item.vacancyDisciplines.toString() + item.vacancyAdditionally
                )
            )
        }
        vacanciesAdapter.update(items)
        vacanciesLoader.visibility = View.GONE
        vacancyList.visibility = View.VISIBLE
        if (Sorting.likeVacancies.toList().isEmpty()) {
            noVacancies.visibility = View.VISIBLE
        }
    }

    override fun showError() {}
}