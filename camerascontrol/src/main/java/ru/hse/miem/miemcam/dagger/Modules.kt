package ru.hse.miem.miemcam.dagger

import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.hse.miem.miemapp.dagger.NetworkUtils
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemcam.CameraSession
import ru.hse.miem.miemcam.data.api.*
import ru.hse.miem.miemcam.data.repositories.*
import ru.hse.miem.miemcam.domain.repositories.*

@Module
class DataModule {

  @CamerasScope
  @Provides
  fun providesCameraSession() = CameraSession()

  @CamerasScope
  @Provides
  fun providesMainApi(session: Session): MainApi
      = createApi(NetworkUtils.createOkHttpClient { addHeader("key", session.token) }, CameraSession.basicAdress, MainApi::class.java)

  @CamerasScope
  @Provides
  fun providesNvrApi(cameraSession: CameraSession): NvrApi
      = createApi(NetworkUtils.createOkHttpClient { addHeader("key", cameraSession.key) } , CameraSession.basicAdressNvr, NvrApi::class.java)

  private fun <T : Api> createApi(client: OkHttpClient, baseUrl: String, apiClass: Class<T>): T = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()
    .create(apiClass)
}

@Module
abstract class RepositoryModule {
  @CamerasScope
  @Binds
  abstract fun bindsIActionRepository(actionRepository: ActionRepository): IActionRepository

  @CamerasScope
  @Binds
  abstract fun bindsICameraRepository(cameraRepository: CameraRepository): ICameraRepository

  @CamerasScope
  @Binds
  abstract fun bindsIPresetRepository(presetRepository: PresetRepository): IPresetRepository

  @CamerasScope
  @Binds
  abstract fun bindsIRecordRepository(recordRepository: RecordRepository): IRecordRepository

  @CamerasScope
  @Binds
  abstract fun bindsIVmixRepository(vmixRepository: VmixRepository): IVmixRepository

  @CamerasScope
  @Binds
  abstract fun bindsIAuthRepository(authRepository: AuthRepository): IAuthRepository
}