package ru.hse.miem.miemapp.dagger

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.noties.markwon.Markwon
import io.noties.markwon.image.glide.GlideImagesPlugin
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.RuzApi
import ru.hse.miem.miemapp.data.repositories.*
import ru.hse.miem.miemapp.domain.repositories.*
import timber.log.Timber
import javax.inject.Singleton

@Module
class GoogleModule {

    @Provides
    @Singleton
    fun providesGetSignedGoogleAccount(application: Application): () -> GoogleSignInAccount = {
        GoogleSignIn.getLastSignedInAccount(application) ?: GoogleSignInAccount.createDefault()
    }


    @Provides
    fun provideGoogleSingInOptions(application: Application): GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(application.getString(R.string.cabinet_client_id))
            .requestEmail()
            .requestProfile()
            .build()

    @Provides
    fun provideGoogleSignInClient(application: Application, gso: GoogleSignInOptions): GoogleSignInClient = GoogleSignIn.getClient(application, gso)
}

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindIAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindIProfileRepository(profileRepository: ProfileRepository): IProfileRepository

    @Binds
    @Singleton
    abstract fun bindIProjectRepository(projectRepository: ProjectRepository): IProjectRepository

    @Binds
    @Singleton
    abstract fun bindISearchRepository(searchRepository: SearchRepository): ISearchRepository

    @Binds
    @Singleton
    abstract fun bindIVacancyRepository(vacancyRepository: VacancyRepository): IVacancyRepository

    @Binds
    @Singleton
    abstract fun bindIScheduleRepository(scheduleRepository: ScheduleRepository): IScheduleRepository

    @Binds
    @Singleton
    abstract fun bindISandboxRepository(sandboxRepository: SandboxRepository): ISandboxRepository
}

@Module(includes = [GoogleModule::class])
class DataModule {

    @Provides
    @Singleton
    fun provideCabinetApi(session: Session, gson: Gson): CabinetApi = Retrofit.Builder()
        .baseUrl(CabinetApi.CABINET_BASE_URL)
        .client(
            NetworkUtils.createOkHttpClient(
                requestSettings = { addHeader("x-auth-token", session.token) },
                responseSettings = {}
            )
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(CabinetApi::class.java)

    @Provides
    @Singleton
    fun provideRuzApi(session: Session, gson: Gson): RuzApi = Retrofit.Builder()
        .baseUrl(RuzApi.RUZ_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(RuzApi::class.java)

    @Provides
    @Singleton
    fun provideSession() = Session()

    @Provides
    fun providesGson() = GsonBuilder()
        .serializeNulls()
        .create()
}

@Module
class MiscModule {

    @Provides
    fun provideMarkwon(application: Application) = Markwon.builder(application)
        .usePlugin(GlideImagesPlugin.create(application))
        .build()
}

object NetworkUtils {

    fun createOkHttpClient(
        requestSettings: Request.Builder.() -> Unit = {},
        responseSettings: Response.() -> Unit = {}
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(createInterceptor(requestSettings, responseSettings))
        .retryOnConnectionFailure(true)
        .build()

    private fun createInterceptor(
        requestSettings: Request.Builder.() -> Unit = {},
        responseSettings: Response.() -> Unit = {}
    ) = Interceptor {
        it.run {
            val requestBody = Buffer().also {
                request().body?.writeTo(it)
            }.readUtf8()
            Timber.tag("OkHttp/Request")
            Timber.d("${request()} $requestBody")

            proceed(
                request()
                    .newBuilder()
                    .apply { requestSettings() }
                    .addHeader("Connection", "close")
                    .addHeader("User-Agent", "Miem App") // used on server side
                    .build()
            ).apply {
                Timber.tag("OkHttp/Response")
                Timber.d("$this ${peekBody(Long.MAX_VALUE).string()}")
                responseSettings()
            }
        }
    }
}