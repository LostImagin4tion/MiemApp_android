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
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.tinder.Sorting
import java.util.ArrayList
import javax.inject.Inject

class VacanciesFragment : BaseFragment(R.layout.fragment_vacancies), ViewAllView {
    @Inject
    @InjectPresenter
    lateinit var vacanciesPresenter: VacanciesPresenter

    @ProvidePresenter
    fun provideVacanciesPresenter() = vacanciesPresenter

    // TO-DO
//    private val args: TinderFragmentArgs by navArgs()

    private val vacanciesAdapter = VacanciesAdapter {
        val action = VacanciesFragmentDirections.actionFragmentVacanciesToFragmentProject(it)
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectsList.adapter = vacanciesAdapter
        if (vacanciesAdapter.hasData) {
            viewAllLoader.visibility = View.GONE
            projectsList.visibility = View.VISIBLE
        }

        vacanciesPresenter.onCreate()
    }

    override fun setupLovedVacancies(vacancies: List<Vacancies>) {

//        for (vacancy_id in lovedVacancies) {
//            val lol : List<String> = listOf("lol")
//            var temp = Vacancies(vacancy_id.toLong(),2131,"fdg","dsf",lol,lol)
//
//            trueVacancies.add(temp)
//        }
        for (item in Sorting.likeVacancies) {

            Log.d("vacancy",item.vacancy_role )
            Log.d("vacancy",item.project_id )
            Log.d("vacancy",item.project_name_rus )
            Log.d("vacancy",item.requirements )
        }

        vacanciesAdapter.update(Sorting.likeVacancies)


        viewAllLoader.visibility = View.GONE
        projectsList.visibility = View.VISIBLE
    }

    override fun showError() {}
}