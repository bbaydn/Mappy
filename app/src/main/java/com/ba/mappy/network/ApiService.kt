package com.ba.mappy.network

import com.ba.mappy.model.Station
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("case-study/6/stations")
    fun getStations(): Call<List<Station>>

    @POST("case-study/6/stations/{stationId}/trips/{tripId}")
    fun sendTrip(
        @Path("stationId") stationId: Int,
        @Path("tripId") tripId: Int
    ): Call<Any>


}
