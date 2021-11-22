package com.wundermobility.carrental.network

import com.wundermobility.carrental.models.RentalInfo
import com.wundermobility.carrental.models.RequestCar
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BookRentalService {

    @POST("/default/wunderfleet-recruiting-mobile-dev-quick-rental")
    suspend fun bookQuickRental(@Header("Authorization") authorization:String, @Body requestCar : RequestCar) : RentalInfo

}