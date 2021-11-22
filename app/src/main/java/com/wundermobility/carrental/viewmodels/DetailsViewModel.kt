package com.wundermobility.carrental.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wundermobility.carrental.utils.AuthUtils
import com.wundermobility.carrental.models.CarInfo
import com.wundermobility.carrental.models.RentalInfo
import com.wundermobility.carrental.models.RequestCar
import com.wundermobility.carrental.network.BookRentalServices
import com.wundermobility.carrental.network.CarRentalServices
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.HttpException
import retrofit2.Response

class DetailsViewModel : ViewModel() {

    var carDetailsLiveData: MutableLiveData<Response<CarInfo>> = MutableLiveData()
    var rentalInfoLiveData: MutableLiveData<Response<RentalInfo>> = MutableLiveData()

    suspend fun fetchCarDetails(carId: Int) {

        try {
            val carDetailsResult: Deferred<Response<CarInfo>> = viewModelScope.async {
                (CarRentalServices.service.getCarDetails(carId))
            }

            val response = carDetailsResult.await()
            if (response.isSuccessful && response.code() == 200) {
                carDetailsLiveData.postValue(response)
            }else{
                carDetailsLiveData.postValue(response)
            }

        } catch (httpException: HttpException) {

            carDetailsLiveData.postValue(null)
        }
    }

    suspend fun bookRental(carId: Int) {

        val requestCar = RequestCar(carId)

        try {
            val rentalCarResult : Deferred<Response<RentalInfo>> = viewModelScope.async {
                BookRentalServices.service.bookQuickRental(
                    AuthUtils.getAuthenticationHeader(),
                    requestCar
                )
            }

            val response = rentalCarResult.await()
            if (response.isSuccessful && response.code() == 200) {
                rentalInfoLiveData.postValue(response)
            }else{
                rentalInfoLiveData.postValue(response)
            }

        } catch (httpException: HttpException) {

            carDetailsLiveData.postValue(null)
        }

    }


}