package ru.hse.miem.miemapp.dagger

import android.app.Application
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.repositories.AuthRepository
import ru.hse.miem.miemapp.data.repositories.ProfileRepository
import ru.hse.miem.miemapp.data.repositories.ProjectRepository
import ru.hse.miem.miemapp.data.repositories.SearchRepository
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
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
                responseSettings = {
                    val body = peekBody(Long.MAX_VALUE).string()

                    /*
                    If error was occurred with response we want to see an exception.
                    However, api returns code 200 even for invalid requests (of course, it's very stupid).
                    So here's a workaround for detecting error response
                     */
                    gson.fromJson(body, JsonObject::class.java)
                        .get("code")
                        ?.asInt
                        ?.takeIf { it / 1000 > CabinetApi.SUCCESS_CODE_PREFIX }
                        ?.let { throw IllegalAccessException("Unauthorized") }
                }
            )
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CabinetApi::class.java)

    @Provides
    @Singleton
    fun provideSession() = Session()

    @Provides
    fun providesGson() = GsonBuilder()
        .serializeNulls()
        .create()
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
            Log.d("OkHttp/Request", "${request()} $requestBody")

            proceed(
                request()
                    .newBuilder()
                    .apply { requestSettings() }
                    .addHeader("Connection", "close")
                    .addHeader("User-Agent", "Miem App") // used on server side
                    .build()
            ).apply {
                Log.i("OkHttp/Response", "$this ${peekBody(Long.MAX_VALUE).string()}")
                responseSettings()
            }
        }
    }
}