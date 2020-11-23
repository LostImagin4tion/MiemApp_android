package ru.hse.miem.miemapp.data.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CabinetApi {

    companion object {
        const val CABINET_BASE_URL = "https://cabinet.miem.hse.ru/"
        private const val api = "api"
        private const val publicApi = "public-api"

        fun getAvatarUrl(userId: Long) = "$CABINET_BASE_URL$publicApi/user/$userId/avatar"
    }

    // auth endpoint
    @POST("vue/google")
    fun auth(@Body authRequest: AuthRequest): Single<AuthResponse>

    @GET("$api/student_profile")
    fun myStudentProfile(): Single<StudentProfileResponse>

    @GET("$api/student_profile")
    fun myTeacherProfile(): Single<TeacherProfileResponse>

    @GET("$publicApi/student_profile/{id}")
    fun studentProfile(@Path("id") id: Long): Single<StudentProfileResponse>

    @GET("$publicApi/teacher_profile/{id}")
    fun teacherProfile(@Path("id") id: Long): Single<TeacherProfileResponse>


}