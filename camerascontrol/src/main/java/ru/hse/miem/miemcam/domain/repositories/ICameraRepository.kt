package ru.hse.miem.miemcam.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import ru.hse.miem.miemcam.domain.entities.Camera

interface ICameraRepository {
  fun discovery(): Single<List<Camera>>
  fun chooseCam(uid: String): Completable
  fun releaseCamera(): Completable
}