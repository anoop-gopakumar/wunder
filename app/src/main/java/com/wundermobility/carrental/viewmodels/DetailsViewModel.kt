package com.wundermobility.carrental.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wundermobility.carrental.utils.AuthUtils
import com.wundermobility.carrental.models.CarInfo
import com.wundermobility.carrental.models.RentalInfo
import com.wundermobility.carrental.models.RequestCar
import com.wundermobility.carrental.network.BookRentalServices
import com.wundermobility.carrental.network.CarRentalServices
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException

class DetailsViewModel : ViewModel() {

    var carDetailsLiveData: MutableLiveData<CarInfo> = MutableLiveData()
    var rentalInfoLiveData: MutableLiveData<RentalInfo> = MutableLiveData()

    suspend fun fetchCarDetails(carId: Int) {

        try {
            val carDetailsResult = viewModelScope.async {
               (CarRentalServices.service.getCarDetails(carId))
            }
            carDetailsLiveData.postValue(carDetailsResult.await())
        } catch (httpException: HttpException) {
            carDetailsLiveData.postValue(null)
        }
    }

    suspend fun bookRental(carId: Int) {

        val requestCar = RequestCar(carId)

        try {
            val rentalCarResult = viewModelScope.async {
                BookRentalServices.service.bookQuickRental(
                    AuthUtils.getAuthenticationHeader(),
                    requestCar
                )
            }
            rentalInfoLiveData.postValue(rentalCarResult.await())
        } catch (httpException: HttpException) {
            carDetailsLiveData.postValue(null)
        }

    }


}