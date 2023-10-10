package com.ba.mappy.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ba.mappy.R
import com.ba.mappy.adapter.StationAdapter
import com.ba.mappy.model.Station
import com.ba.mappy.network.ApiService
import com.ba.mappy.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListTripsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var stationAdapter: StationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_trips)

        recyclerView = findViewById(R.id.rvListTrips)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val apiService: ApiService = RetrofitClient.create()
        val call: Call<List<Station>> = apiService.getStations()

        call.enqueue(object : Callback<List<Station>> {
            override fun onResponse(
                call: Call<List<Station>>,
                response: Response<List<Station>>
            ) {
                if (response.isSuccessful) {
                    val stationList = response.body()
                    stationList?.let {
                        stationAdapter = StationAdapter(it)
                        recyclerView.adapter = stationAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                // Hata eklenecek
            }
        })

    }





}