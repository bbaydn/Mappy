package com.ba.mappy.network

import com.ba.mappy.model.Station
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("case-study/6/stations")
    fun getStations(): Call<List<Station>>


}
