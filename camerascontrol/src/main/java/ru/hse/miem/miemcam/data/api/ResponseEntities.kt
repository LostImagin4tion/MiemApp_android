package ru.hse.miem.miemcam.data.api

import ru.hse.miem.miemcam.domain.entities.Lesson

data class LoginResponse(
  val token: String
)

data class PersonResponse(
  val type: String,
  val email: String
)

data class RoomListItemResponse(
  val name: String
)

data class TimetableResponse(
  val Count: Int,
  val Lessons: List<Lesson>
)