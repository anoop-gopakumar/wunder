package com.wundermobility.carrental.models

data class CarInfo(
    val carId: Int, val title: String, val licencePlate: String,
    val zipCode: String, val city: String, val lat: Float, val lon: Float, val reservationState: Int,
    val vehicleTypeImageUrl: String ,val address :String ,val fuelLevel : Int
)