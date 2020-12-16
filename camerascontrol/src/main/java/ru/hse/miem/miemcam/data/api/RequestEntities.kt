package ru.hse.miem.miemcam.data.api

data class MoveCamRequest(
  val ptz: List<Float>
)

data class SetFocusContinuousRequest(
  val m_foc: Float
)

data class SetFocusRequest(
  val position: Float,
  val speed: Float
)

data class LoginRequest(
  val email: String,
  val password: String
)

data class ChooseCamRequest(
  val uid: String
)

data class SetPresetRequest(
  val s_pres: Int
)

data class GoToPresetRequest(
  val go_pres: Int
)

data class RequestRecordRequest(
  val start_time: String,
  val end_time: String,
  val date: String,
  val event_name: String,
  val user_email: String
)

data class ExecuteVmixFunctionRequest(
  val Function: String,
  val Input: String?
)

data class ChooseVmixRequest(
  val ip: String,
  val port: Int
)