package ru.hse.miem.miemcam.domain.repositories

import io.reactivex.Completable

enum class SettingType(val value: String) {
  BRIGHTNESS("brightness"),
  CONTRAST("contrast"),
  SATURATION("color_saturation")
}

enum class FocusMode(val value: String) {
  AUTO("\"AUTO\""),
  MANUAL("\"MANUAL\"")
}

interface IActionRepository {
  fun moveCam(dir: ArrayList<Float>): Completable
  fun stop(): Completable
  fun setSetting(type: SettingType, value: Float): Completable
  fun setFocusContinious(value: Float): Completable
  fun stopFocus(): Completable
  fun setFocus(position: Float, speed: Float): Completable
  fun setFocusMode(mode: FocusMode): Completable
}