package com.wundermobility.carrental.network

import com.wundermobility.carrental.models.CarInfo
import com.wundermobility.carrental.models.Cars
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarRentalService {

    @GET("/wunderfleet-recruiting-dev/cars.json")
    suspend fun listAllCars() : List<Cars>

    @GET("/wunderfleet-recruiting-dev/cars/{carId}")
    suspend  fun getCarDetails(@Path("carId") carId: Int) : CarInfo


}