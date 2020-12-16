package ru.hse.miem.miemcam.domain.repositories

import io.reactivex.Completable
import ru.hse.miem.miemcam.domain.entities.Preset

interface IPresetRepository {
  fun setPreset(preset: Preset): Completable
  fun setPreset(id: Int): Completable
  fun execPreset(id: Int): Completable
}