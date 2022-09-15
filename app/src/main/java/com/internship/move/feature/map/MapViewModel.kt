package com.internship.move.feature.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch

class MapViewModel(private val mapService: MapService) : ViewModel() {
    val scooterList: MutableLiveData<List<ScooterResponse>> = MutableLiveData()
    val currentLocation: MutableLiveData<LatLng> = MutableLiveData()

    fun getScooters(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            try {
                scooterList.value = mapService.getAllScooters(latitude = latitude, longitude = longitude)
                logTag("SCOOTER_0", mapService.getAllScooters(latitude = latitude, longitude = longitude)[0].toString())
            } catch (e: Exception) {
                logTag("SCOOTER_ERROR", e.toString())
            }
        }
    }
}