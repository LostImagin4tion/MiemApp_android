package ru.hse.miem.miemcam.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single

interface IRecordRepository {
  fun getRooms(): Single<List<String>>
  fun requestRecord(
    room: String,
    name: String,
    email: String,
    date: String,
    start: String,
    stop: String
  ): Completable
}