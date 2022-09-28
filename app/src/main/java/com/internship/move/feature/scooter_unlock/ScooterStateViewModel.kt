package com.internship.move.feature.scooter_unlock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.feature.map.ScooterResponseDTO
import com.internship.move.model.ErrorResponse
import com.internship.move.utils.logTag
import com.internship.move.utils.toErrorResponse
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.launch

class ScooterStateViewModel(
    private val scooterStateService: ScooterStateService,
    private val errorJsonAdapter: JsonAdapter<ErrorResponse>
) : ViewModel() {

    private val _scooterResult: MutableLiveData<ScooterResponseDTO?> = MutableLiveData()
    val scooterResult: LiveData<ScooterResponseDTO?>
        get() = _scooterResult
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isError: MutableLiveData<String?> = MutableLiveData()
    val isError: LiveData<String?>
        get() = _isError
    private lateinit var rideResult: RideDTO

    fun startScooterUnlock(scooterCode: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _scooterResult.value = scooterStateService.startUnlock(scooterNumber = scooterCode)
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetScooterState() {
        viewModelScope.launch {
            try {
                scooterStateService.lockScooter(_scooterResult.value?.scooterNumber?.toInt() ?: 1000)
                _scooterResult.value = null
                _isError.value = null
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            }
        }
    }

    fun startScooterRide() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val startScooterDTO = StartScooterDTO(
                    scooterNumber = _scooterResult.value?.scooterNumber?.toInt() ?: 1000,
                    longitude = _scooterResult.value?.location?.coordinates?.get(0) ?: CLUJANGELES.longitude,
                    latitude = _scooterResult.value?.location?.coordinates?.get(1) ?: CLUJANGELES.latitude
                )
                logTag("StartRide", startScooterDTO.toString())
                rideResult = scooterStateService.startRide(startScooterDTO)
                logTag("StartRide", rideResult.toString())
            } catch (e: Exception) {
                logTag("StartRideError", e.toErrorResponse(errorJsonAdapter).message)
                resetScooterState()
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun endScooterRIde() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                scooterStateService.endRide(
                    rideId = rideResult.rideId,
                    LocationDTO(
                        _scooterResult.value?.location?.coordinates?.get(0) ?: CLUJANGELES.longitude,
                        _scooterResult.value?.location?.coordinates?.get(1) ?: CLUJANGELES.latitude
                    )
                )
                resetScooterState()
            } catch (e: Exception) {
                logTag("ENDRIDE", e.toErrorResponse(errorJsonAdapter).message)
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun lockScooterRide() {
        viewModelScope.launch {
            try {
                scooterStateService.lockScooter(_scooterResult.value?.scooterNumber?.toInt() ?: 1000)
                _isError.value = null
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            }

        }
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)

    }
}