package ru.hse.miem.miemapp.presentation.sandbox

import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.ISandboxRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

class SandboxPresenter @Inject constructor(
    private val sandboxRepository: ISandboxRepository
) : BasePresenter<SandboxView>() {

    fun onCreate() = launch {
        try {
            sandboxRepository.getSandboxProjects().let(viewState::setupSandbox)
        } catch (e: Exception) {
            proceedError(e)
        }
    }
}