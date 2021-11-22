package com.wundermobility.carrental.utils

import com.wundermobility.carrental.models.CarInfo

object DataUtils {

    fun convertToData(carInfo: CarInfo) : ArrayList<Pair<String, String>>{

        val data = ArrayList<Pair<String, String>>()

        return data.also {
            it.add(Pair("Name", carInfo.title))
            it.add(Pair("Licence", carInfo.licencePlate))
            it.add(Pair("Address", carInfo.address))
            it.add(Pair("Zipcode", carInfo.zipCode))
            it.add(Pair("Reservation State", carInfo.reservationState.toString()))
            it.add(Pair("Fuel Level", carInfo.fuelLevel.toString()))
        }

    }

}