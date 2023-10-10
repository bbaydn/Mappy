package com.ba.mappy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ba.mappy.databinding.ActivityMapsBinding
import com.ba.mappy.presentation.ListTripsActivity
import com.ba.mappy.viewmodel.StationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val stationViewModel: StationViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonListTrips.setOnClickListener {
            val intent = Intent(this, ListTripsActivity::class.java)
            startActivity(intent)
        }

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            setupMap()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
        }
    }

    private fun setupMap() {
        val width = 45
        val height = 45
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        val radius = Math.min(width, height) / 2.toFloat()
        canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, paint)

        val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    if (location != null) {
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(userLatLng, 15f),
                            2000,
                            null
                        )
                        mMap.addMarker(MarkerOptions().position(userLatLng).title("My Location"))
                    }
                }
        }

        stationViewModel.stationsLiveData.observe(this) { stations ->
            val gson = Gson()
            // Convert the list to a JSON string
            val json = gson.toJsonTree(stations).asJsonArray
            Log.e("BUSRA", json.toString())
            if (json != null) {
                Log.d("Get From API", stations.toString())
                for (i in 0 until json.size()) {
                    val jsonarrays = json[i].asJsonObject
                    val centerCoordinates = jsonarrays["center_coordinates"].asString
                    val (latitudeStr, longitudeStr) = centerCoordinates.toString().split(",")
                    val latitude = latitudeStr.toDouble()
                    val longitude = longitudeStr.toDouble()
                    val location = LatLng(latitude, longitude)
                    val name = jsonarrays.get("name")

                    mMap.addMarker(
                        MarkerOptions()
                            .position(location).title(name.toString())
                            .icon(markerIcon)
                    )
                }
            } else {
                Log.e("BUSRA", "this is completely wrong")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}
