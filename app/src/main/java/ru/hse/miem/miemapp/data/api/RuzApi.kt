package ru.hse.miem.miemapp.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface RuzApi {

    companion object {

        const val RUZ_BASE_URL = "https://ruz.hse.ru/"

        private const val api = "api"
    }

    @GET("$api/schedule/student/?")
    suspend fun studentSchedule(
        @Query("email") userEmail: String,
        @Query("start") startDate: String,
        @Query("finish") finishDate: String,
        @Query("lng") lng: String
    ): List<ScheduleResponse>

    @GET("$api/schedule/person/?")
    suspend fun staffSchedule(
        @Query("email") userEmail: String,
        @Query("start") startDate: String,
        @Query("finish") finishDate: String,
        @Query("lng") lng: String
    ): List<ScheduleResponse>
}