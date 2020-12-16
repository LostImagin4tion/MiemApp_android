package ru.hse.miem.miemcam.data.repositories

import ru.hse.miem.miemcam.data.api.NvrApi
import ru.hse.miem.miemcam.data.api.RequestRecordRequest
import ru.hse.miem.miemcam.domain.repositories.IRecordRepository
import javax.inject.Inject

class RecordRepository @Inject constructor(private val nvrApi: NvrApi) : IRecordRepository {

  override fun getRooms() = nvrApi.getRooms().map { it.map { it.name } }

  override fun requestRecord(
    room: String,
    name: String,
    email: String,
    date: String,
    start: String,
    stop: String
  ) = nvrApi.requestRecord(
    room, RequestRecordRequest(
      start_time = start,
      end_time = stop,
      date = date,
      event_name = name,
      user_email = email
    )
  )

}