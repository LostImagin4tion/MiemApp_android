package ru.hse.miem.miemapp.data.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CabinetApi {

    companion object {
        const val CABINET_BASE_URL = "https://cabinet.miem.hse.ru/"
        private const val api = "api"
        private const val publicApi = "public-api"
    }

    // auth endpoint
    @POST("vue/google")
    fun auth(@Body authRequest: AuthRequest): Single<AuthResponse>

}