package com.wundermobility.carrental.models

data class RentalInfo(
    val reservationId: Int,
    val carId: Int,
    val cost: Int,
    val drivenDistance: Int,
    val startAddress: String,
    val userId: Int,
    val endTime: Long,
    val startTime: Long
)