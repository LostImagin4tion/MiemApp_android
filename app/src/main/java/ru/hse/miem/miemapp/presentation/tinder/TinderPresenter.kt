package ru.hse.miem.miemapp.presentation.tinder

import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.IVacancyRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import java.lang.Exception
import javax.inject.Inject

class TinderPresenter @Inject constructor(
    private val vacancyRepository: IVacancyRepository
) : BasePresenter<InfoView>() {

    fun onCreate() = launch {
        try {
            vacancyRepository.getAllVacancies().let(viewState::setupVacancies)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

}