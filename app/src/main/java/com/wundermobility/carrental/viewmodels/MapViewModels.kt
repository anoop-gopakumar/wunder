package com.wundermobility.carrental.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wundermobility.carrental.models.Cars
import com.wundermobility.carrental.network.CarRentalServices
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapViewModels : ViewModel() {

    // private val carListLiveData: LiveData<List<Cars>> = _carListLiveData
    // private var carDetailsLiveData: LiveData<CarInfo> = MutableLiveData()
    // private var rentalInfoLiveData: LiveData<RentalInfo> = MutableLiveData()

    val _carListLiveData: MutableLiveData<List<Cars>> = MutableLiveData()

    init {
        viewModelScope.launch(IO) {
            fetchCarListData()
        }
    }

    suspend fun fetchCarListData(): MutableLiveData<List<Cars>> {
        val carListResult = viewModelScope.async {
            CarRentalServices.service.listAllCars()
        }

        _carListLiveData.postValue(carListResult.await())
        return _carListLiveData
    }

}