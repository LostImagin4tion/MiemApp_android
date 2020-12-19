package ru.hse.miem.miemcam.data.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.hse.miem.miemcam.domain.entities.Camera
import ru.hse.miem.miemcam.domain.entities.Preset
import ru.hse.miem.miemcam.domain.entities.VmixState

interface MainApi : Api {

  @POST("continuous_move")
  fun moveCam(@Body moveCamRequest: MoveCamRequest): Completable

  @GET("stop")
  fun stop(): Completable

  @POST("set_{settingType}")
  fun setSetting(@Path("settingType") settingType: String, @Body value: Float): Completable

  @POST("move_focus_continuous")
  fun setFocusContinuous(@Body setFocusContinuousRequest: SetFocusContinuousRequest): Completable

  @GET("stop_focus")
  fun stopFocus(): Completable

  @POST("move_focus_absolute")
  fun setFocus(@Body setFocusRequest: SetFocusRequest): Completable

  @POST("set_focus_mode")
  fun setFocusMode(@Body mode: String): Completable

  @POST("login")
  fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>

  @GET("login_token")
  fun loginToken(): Completable

  @GET("return_cams")
  fun getCams(): Single<List<Camera>>

  @POST("chose_cam")
  fun chooseCam(@Body chooseCamRequest: ChooseCamRequest): Completable

  @GET("release_cam")
  fun releaseCam(): Completable

  @POST("Cam_set_preset")
  fun setPreset(@Body preset: Preset): Completable

  @POST("set_preset")
  fun setPreset(@Body setPresetRequest: SetPresetRequest): Completable

  @POST("goto_preset")
  fun goToPreset(@Body goToPresetRequest: GoToPresetRequest): Completable

  @GET("vmix")
  fun executeVmixFunction(@Body executeVmixFunctionRequest: ExecuteVmixFunctionRequest): Completable

  @GET("vmixInfo")
  fun getVmixInfo(): Single<VmixState>

  @GET("chose_vmix")
  fun chooseVmix(@Body chooseVmixRequest: ChooseVmixRequest): Completable
}