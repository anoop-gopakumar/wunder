package com.wundermobility.carrental.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun getLogger(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    return client
}


var retrofitCar: Retrofit = Retrofit.Builder()
    .baseUrl("https://s3.eu-central-1.amazonaws.com")
    .addConverterFactory(GsonConverterFactory.create())
    .client(getLogger())
    .build()


var retrofitRental: Retrofit = Retrofit.Builder()
    .baseUrl("https://4i96gtjfia.execute-api.eu-central-1.amazonaws.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()


object CarRentalServices {

    var service: CarRentalService = retrofitCar.create(CarRentalService::class.java)

}

object BookRentalServices {

    var service: BookRentalService = retrofitRental.create(BookRentalService::class.java)

}



