package ru.hse.miem.miemapp.presentation.vacancies


import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.hse.miem.miemapp.domain.repositories.IVacancyRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import java.lang.Exception
import javax.inject.Inject

@InjectViewState
class VacanciesPresenter @Inject constructor(
    private val vacancyRepository: IVacancyRepository
) : BasePresenter<VacanciesView>() {

    fun onCreate() = launch {
        try {
            vacancyRepository.getAllVacancies().let(viewState::setupLovedVacancies)
        } catch (e: Exception) {
        }
    }

}