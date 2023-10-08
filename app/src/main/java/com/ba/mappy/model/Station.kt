package com.ba.mappy.model

data class Station(
    val center_coordinates: String,
    val id: Int,
    val name: String,
    val trips: List<Trip>,
    val trips_count: Int
)
