package ru.hse.miem.miemcam.data.repositories

import io.reactivex.Completable
import ru.hse.miem.miemcam.domain.entities.Preset
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.data.api.GoToPresetRequest
import ru.hse.miem.miemcam.data.api.MainApi
import ru.hse.miem.miemcam.data.api.SetPresetRequest
import ru.hse.miem.miemcam.domain.repositories.IPresetRepository
import javax.inject.Inject

class PresetRepository @Inject constructor(
  private val mainApi: MainApi,
  private val cameraSession: CameraSession
) : IPresetRepository {

  override fun setPreset(preset: Preset): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.setPreset(preset)
  }

  override fun setPreset(id: Int): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.setPreset(SetPresetRequest(id))
  }

  override fun execPreset(id: Int): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.goToPreset(GoToPresetRequest(id))
  }
}