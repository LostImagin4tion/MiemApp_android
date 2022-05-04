package ru.hse.miem.miemapp.presentation.search

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import ru.hse.miem.miemapp.presentation.search.db.SearchDbManager
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val searchRepository: ISearchRepository
) : BasePresenter<SearchView>() {

    fun onCreate() = launch {
        try {
            searchRepository.getAllProjects().let(viewState::setupProjects)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    fun loadFromDb(dbManager: SearchDbManager) = launch {
        try {
            dbManager.openDb()
            dbManager.readDb().let(viewState::loadProjects)
        } catch (e: Exception) {
            dbManager.upgradeDb()
            dbManager.readDb().let(viewState::loadProjects)
        }
    }
}