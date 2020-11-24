package ru.hse.miem.miemapp.dagger

import android.app.Application
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
    fun provideCabinetApi(client: OkHttpClient): CabinetApi = Retrofit.Builder()
            .baseUrl(CabinetApi.CABINET_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CabinetApi::class.java)

    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()

    @Provides
    fun provideInterceptor(session: Session) = Interceptor {
        it.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Connection", "close")
                    .addHeader("User-Agent", "Miem App") // used on server side
                    .addHeader("x-auth-token", session.token)
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    fun provideSession() = Session()
}