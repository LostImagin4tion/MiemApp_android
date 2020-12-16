package ru.hse.miem.miemcam.data.repositories

import io.reactivex.Completable
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.data.api.MainApi
import ru.hse.miem.miemcam.data.api.MoveCamRequest
import ru.hse.miem.miemcam.data.api.SetFocusContinuousRequest
import ru.hse.miem.miemcam.data.api.SetFocusRequest
import ru.hse.miem.miemcam.domain.repositories.FocusMode
import ru.hse.miem.miemcam.domain.repositories.IActionRepository
import ru.hse.miem.miemcam.domain.repositories.SettingType
import javax.inject.Inject

class ActionRepository @Inject constructor(
    private val cameraSession: CameraSession,
    private val mainApi: MainApi
) : IActionRepository {

  override fun moveCam(dir: ArrayList<Float>): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.moveCam(MoveCamRequest(dir))
  }

  override fun stop(): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.stop()
  }

  override fun setSetting(type: SettingType, value: Float): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.setSetting(type.value, value)
  }

  override fun setFocusContinious(value: Float): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.setFocusContinuous(SetFocusContinuousRequest(value))
  }

  override fun stopFocus(): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.stopFocus()
  }

  override fun setFocus(position: Float, speed: Float): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.setFocus(SetFocusRequest(position, speed))
  }

  override fun setFocusMode(mode: FocusMode): Completable {
    if (cameraSession.pickedCamera.isEmpty()) {
      return Completable.complete()
    }
    return mainApi.setFocusMode(mode.value)
  }
}