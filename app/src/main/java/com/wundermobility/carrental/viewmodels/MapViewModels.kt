package com.wundermobility.carrental.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wundermobility.carrental.models.Cars
import com.wundermobility.carrental.network.CarRentalServices
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class MapViewModels : ViewModel() {

    val carListLiveData: MutableLiveData<Response<List<Cars>>> = MutableLiveData()

    init {
        viewModelScope.launch(IO) {
            fetchCarListData()
        }
    }

    private suspend fun fetchCarListData() {

        try {

            val carListResult: Deferred<Response<List<Cars>>> = viewModelScope.async {
                CarRentalServices.service.listAllCars()
            }

            val response = carListResult.await()

            if (response.isSuccessful && response.code() == 200) {
                carListLiveData.postValue(response)
            } else {
                carListLiveData.postValue(response)
            }

        } catch (httpException: HttpException) {
            carListLiveData.postValue(null)
        }

    }

}