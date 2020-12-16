package ru.hse.miem.miemcam.data.repositories

import ru.hse.miem.miemcam.data.api.ChooseCamRequest
import ru.hse.miem.miemcam.data.api.MainApi
import ru.hse.miem.miemcam.domain.repositories.ICameraRepository
import javax.inject.Inject

class CameraRepository @Inject constructor(private val mainApi: MainApi) : ICameraRepository {

  override fun discovery() = mainApi.getCams()

  override fun chooseCam(uid: String) =  mainApi.chooseCam(ChooseCamRequest(uid))

  override fun releaseCamera() = mainApi.releaseCam()

}