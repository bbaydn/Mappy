package com.ba.mappy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ba.mappy.R
import com.ba.mappy.model.Station
import com.ba.mappy.network.ApiService
import com.ba.mappy.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationAdapter(private val stationList: List<Station>) :
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    inner class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val buttonBook: Button = itemView.findViewById(R.id.buttonBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_item, parent, false)
        return StationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationList[position]
        holder.nameTextView.text = station.name
        for (i in 0 until station.trips_count) {
            holder.addressTextView.text = station.trips[i].time
        }
        holder.buttonBook.setOnClickListener {
            for (i in 0 until station.trips_count) {
                onBook(station.id, station.trips[i].id, holder.itemView.context)
            }
        }
    }

    private fun onBook(stationId: Int, tripId: Int, context: Context) {
        val apiService: ApiService = RetrofitClient.create()
        val call: Call<Any> = apiService.sendTrip(stationId, tripId)

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    //TODO
                } else {
                    showPopup("The trip  selected is full", "Please select another one", context)
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // Hata eklenecek
            }
        })
    }

    override fun getItemCount(): Int {
        return stationList.size
    }

    private fun showPopup(situation: String, suggestion: String, context: Context) {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.popup_layout, null)

        val tvSituation: TextView = dialogView.findViewById(R.id.tvSituation)
        tvSituation.text = situation
        val tvSuggestion: TextView = dialogView.findViewById(R.id.tvSuggestion)
        tvSuggestion.text = suggestion
        val buttonSelectAnother : Button = dialogView.findViewById(R.id.buttonSelect)



        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context )
        alertDialogBuilder.setView(dialogView)
            .setCancelable(false)


        val alertDialog: AlertDialog = alertDialogBuilder.create()
        buttonSelectAnother.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}
