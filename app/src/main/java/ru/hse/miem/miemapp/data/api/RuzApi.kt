package ru.hse.miem.miemapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface RuzApi {

    companion object {

        const val RUZ_BASE_URL = "https://ruz.hse.ru/"

        private const val api = "api"
    }

    @GET("$api/schedule/student/?email={email}&start={start}&finish={finish}&lng=1")
    suspend fun getStudentSchedule(
        @Path("email") userEmail: String,
        @Path("start") startDate: String,
        @Path("finish") finishDate: String
    ): ScheduleResponse

    @GET("$api/schedule/person/?email={email}start={start}&finish={finish}&lng=1")
    suspend fun getStaffSchedule(
        @Path("email") userEmail: String,
        @Path("start") startDate: String,
        @Path("finish") finishDate: String
    ): ScheduleResponse
}