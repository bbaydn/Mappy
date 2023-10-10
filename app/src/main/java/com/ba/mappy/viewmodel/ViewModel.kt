package com.ba.mappy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ba.mappy.model.Station
import com.ba.mappy.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class StationViewModel : ViewModel() {

    private val _stationsLiveData = MutableLiveData<List<Station>?>()
    val stationsLiveData: MutableLiveData<List<Station>?> = _stationsLiveData

    init {
        fetchStations()
    }

    private fun fetchStations() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiService = RetrofitClient.create()
                val response: Response<List<Station>> = apiService.getStations().execute()

                if (response.isSuccessful) {
                    val stations = response.body()
                    _stationsLiveData.postValue(stations)
                } else {
                    // Hata durumu kodu eklenecek
                }
            } catch (e: Exception) {
                // Hata durumu kodu eklenecek
            }
        }
    }
}
