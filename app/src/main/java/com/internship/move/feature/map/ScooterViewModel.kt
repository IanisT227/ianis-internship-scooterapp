package com.internship.move.feature.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch

class ScooterViewModel(private val scooterService: ScooterService) : ViewModel() {

    private val _scooterList: MutableLiveData<List<ScooterResponseDTO>> = MutableLiveData()
    val scooterList: LiveData<List<ScooterResponseDTO>>
        get() = _scooterList
    val currentLocation: MutableLiveData<LatLng> = MutableLiveData()

    fun getScooters(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            try {
                _scooterList.value = scooterService.getAllScooters(latitude = latitude, longitude = longitude)
                logTag("SCOOTER_0", scooterService.getAllScooters(latitude = latitude, longitude = longitude).getOrNull(0).toString())
            } catch (e: Exception) {
                logTag("SCOOTER_ERROR", e.toString())
            }
        }
    }
}