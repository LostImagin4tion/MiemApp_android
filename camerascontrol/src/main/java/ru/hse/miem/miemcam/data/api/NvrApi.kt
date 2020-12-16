package ru.hse.miem.miemcam.data.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NvrApi : Api {

  @GET("rooms")
  fun getRooms(): Single<List<RoomListItemResponse>>

  @POST("montage-event/{room}")
  fun requestRecord(@Path("room") room: String, @Body requestRecordRequest: RequestRecordRequest): Completable
}