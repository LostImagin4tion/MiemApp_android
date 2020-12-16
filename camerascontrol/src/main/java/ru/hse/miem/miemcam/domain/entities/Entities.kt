package ru.hse.miem.miemcam.domain.entities

// import kotlinx.serialization.Serializable

// @Serializable
data class Camera(
  val uid: String,
  val name: String,
  val room: String
)

// @Serializable
data class Lesson(
  val auditorium: String,
  val lessonNumberStart: Int,
  val beginLesson: String,
  val endLesson: String,
  val date: String,
  val dayOfWeekString: String,
  val building: String,
  val discipline: String,
  val kindOfWork: String?,
  val url1: String?,
  val lecturer: String
)

// @Serializable
data class Preset(
  val id: String,
  val name: String,
  val color: ArrayList<Int>
)

// @Serializable
data class VmixState(
  val overlay1: String?,
  val overlay2: String?,
  val overlay3: String?,
  val overlay4: String?,
  val overlay5: String?,
  val overlay6: String?,
  val preview: String?,
  val active: String?
)