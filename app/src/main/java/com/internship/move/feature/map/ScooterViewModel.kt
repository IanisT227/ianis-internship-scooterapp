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

    fun getScooterById(id: String): ScooterResponseDTO? {
        _scooterList.value?.forEach { scooter ->
            if (scooter.id == id)
                return scooter
        }
        return null
    }

    fun getMarkerItemsList(): List<MarkerItem> {
        val list: MutableList<MarkerItem> = mutableListOf()
        _scooterList.value?.forEach { scooterItem ->
            val markerItem = MarkerItem(
                scooterItem.location.coordinates[1],
                scooterItem.location.coordinates[0],
                scooterItem.scooterNumber,
                scooterItem.id
            )
            list.add(markerItem)
        }
        logTag("ScooterList", list.toString())
        return list
    }
}