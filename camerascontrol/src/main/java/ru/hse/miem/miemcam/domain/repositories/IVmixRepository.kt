package ru.hse.miem.miemcam.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import ru.hse.miem.miemcam.domain.entities.VmixState

interface IVmixRepository {
  fun executeFunction(function: String, input: String?): Completable
  fun getState(): Single<VmixState>
  fun chooseVmix(ip: String, port: Int): Completable
}