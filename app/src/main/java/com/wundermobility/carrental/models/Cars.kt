package com.wundermobility.carrental.models

data class Cars(
    val carId: Int,
    var title: String?,
    val lat: Float,
    val lon: Float,
    val licencePlate: String,
    val fuelLevel: Int,
    val reservationState: String,
    val city: String,
    val address: String
)