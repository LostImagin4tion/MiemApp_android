package ru.hse.miem.miemapp.dagger

import android.app.Application
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.repositories.*
import ru.hse.miem.miemapp.domain.repositories.*
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
}

@Module(includes = [GoogleModule::class])
class DataModule {

    @Provides
    @Singleton
    fun provideCabinetApi(session: Session): CabinetApi = Retrofit.Builder()
            .baseUrl(CabinetApi.CABINET_BASE_URL)
            .client(NetworkUtils.createOkHttpClient { addHeader("x-auth-token", session.token) })
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CabinetApi::class.java)

    @Provides
    @Singleton
    fun provideSession() = Session()
}

object NetworkUtils {

    fun createOkHttpClient(settings: Request.Builder.() -> Unit): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(createInterceptor(settings))
        .retryOnConnectionFailure(true)
        .build()

    private fun createInterceptor(settings: Request.Builder.() -> Unit) = Interceptor {
        it.run {
            val requestBody = Buffer().also {
                request().body?.writeTo(it)
            }.readUtf8()
            Log.d("OkHttp/Request", "${request()} $requestBody")

            proceed(
                request()
                    .newBuilder()
                    .apply { settings() }
                    .addHeader("Connection", "close")
                    .addHeader("User-Agent", "Miem App") // used on server side
                    .build()
            ).apply {
                Log.i("OkHttp/Response", "$this ${peekBody(Long.MAX_VALUE).string()}")
            }
        }
    }
}