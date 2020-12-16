package ru.hse.miem.miemcam.data.repositories

import ru.hse.miem.miemcam.data.api.ChooseVmixRequest
import ru.hse.miem.miemcam.data.api.ExecuteVmixFunctionRequest
import ru.hse.miem.miemcam.data.api.MainApi
import ru.hse.miem.miemcam.domain.repositories.IVmixRepository
import javax.inject.Inject

class VmixRepository @Inject constructor(private val mainApi: MainApi) : IVmixRepository {

  override fun executeFunction(function: String, input: String?) = mainApi.executeVmixFunction(ExecuteVmixFunctionRequest(function, input))

  override fun getState() = mainApi.getVmixInfo()

  override fun chooseVmix(ip: String, port: Int) = mainApi.chooseVmix(ChooseVmixRequest(ip, port))
}