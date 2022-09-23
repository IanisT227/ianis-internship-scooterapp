package com.internship.move.feature.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch

class ScooterViewModel(private val scooterService: ScooterService) : ViewModel() {

    private val _scooterList: MutableLiveData<List<ScooterResponseDTO>> = MutableLiveData()
    val scooterList: LiveData<List<ScooterResponseDTO>>
        get() = _scooterList

    fun getScooters(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            try {
                _scooterList.value = scooterService.getAllScooters(latitude = latitude, longitude = longitude)
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

    fun getMarkerItemsList(scooters: List<ScooterResponseDTO>): List<MarkerItem> {
        return scooters.map { scooterItem ->
            MarkerItem(
                scooterItem.location.coordinates[1],
                scooterItem.location.coordinates[0],
                scooterItem.id,
                scooterItem.scooterNumber
            )
        }
    }
}