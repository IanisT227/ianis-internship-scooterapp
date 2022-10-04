package com.internship.move.feature.scooter_unlock

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.feature.map.CoordinatesDTO
import com.internship.move.feature.map.ScooterResponseDTO
import com.internship.move.feature.menu.rideHistory.RideHistoryItemDTO
import com.internship.move.model.ErrorResponse
import com.internship.move.utils.getScooterAddress
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
    private val _rideDistance: MutableLiveData<Int> = MutableLiveData()
    val rideDistance: LiveData<Int>
        get() = _rideDistance
    private lateinit var rideResult: RideDTO
    private val _lastRide: MutableLiveData<RideHistoryItemDTO?> = MutableLiveData()
    val lastRide: LiveData<RideHistoryItemDTO?>
        get() = _lastRide

    fun startScooterUnlock(scooterCode: Int) {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                _isLoading.value = true
                _scooterResult.value = scooterStateService.startUnlock(scooterNumber = scooterCode)
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun finishRidePayment(){
        _lastRide.value = null
    }

    fun resetScooterState() {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                scooterStateService.lockScooter(_scooterResult.value?.scooterNumber?.toInt() ?: 1000)
                _scooterResult.value = null
                _isError.postValue(null)
                _rideDistance.value = 0
            } catch (e: Exception) {
                logTag("ENDRIDE_RESET", e.toErrorResponse(errorJsonAdapter).message)
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            }
        }
    }

    fun startScooterRide(context: Context) {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                _isLoading.value = true
                val startScooterDTO = StartScooterDTO(
                    scooterNumber = _scooterResult.value?.scooterNumber?.toInt() ?: 1000,
                    longitude = _scooterResult.value?.location?.coordinates?.get(0) ?: CLUJANGELES.longitude,
                    latitude = _scooterResult.value?.location?.coordinates?.get(1) ?: CLUJANGELES.latitude,
                    startAddress = getScooterAddress(
                        CoordinatesDTO(
                            listOf(
                                _scooterResult.value?.location?.coordinates?.get(0) ?: CLUJANGELES.longitude,
                                _scooterResult.value?.location?.coordinates?.get(1) ?: CLUJANGELES.latitude
                            )
                        ), context
                    )
                )
                rideResult = scooterStateService.startRide(startScooterDTO)
                _rideDistance.value = 0
            } catch (e: Exception) {
                logTag("StartRideError", e.toErrorResponse(errorJsonAdapter).message)
                resetScooterState()
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun endScooterRIde(context: Context) {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                _isLoading.value = true
                _lastRide.value = scooterStateService.endRide(
                    rideId = rideResult.rideId,
                    location = EndRideDTO(
                        _scooterResult.value?.location?.coordinates?.get(0) ?: CLUJANGELES.longitude,
                        _scooterResult.value?.location?.coordinates?.get(1) ?: CLUJANGELES.latitude,
                        getScooterAddress(
                            CoordinatesDTO(
                                listOf(
                                    _scooterResult.value?.location?.coordinates?.get(0) ?: CLUJANGELES.longitude,
                                    _scooterResult.value?.location?.coordinates?.get(1) ?: CLUJANGELES.latitude
                                )
                            ), context
                        )
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
                _isError.postValue(null)
                scooterStateService.lockScooter(_scooterResult.value?.scooterNumber?.toInt() ?: 1000)
                _isError.value = null
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
                logTag("ENDRIDE_LOCK", e.toErrorResponse(errorJsonAdapter).message)
            }
        }
    }

    fun unlockScooterRide() {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                scooterStateService.unlockScooter(_scooterResult.value?.scooterNumber?.toInt() ?: 1000)
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            }
        }
    }

    fun updateRideLocation(currentLocation: LatLng) {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                _rideDistance.value = scooterStateService.updateRideLocation(
                    LocationDTO(
                        currentLocation.longitude,
                        currentLocation.latitude
                    )
                ).distance.toInt()
            } catch (e: Exception) {
                logTag("UpdateRideError", e.message.toString())
            }
        }
    }

    fun pingScooter(scooterNumber: String, currentLocation: LocationDTO) {
        viewModelScope.launch {
            try {
                _isError.postValue(null)
                scooterStateService.pingScooter(scooterNumber = scooterNumber.toInt(), currentLocation)
            } catch (e: Exception) {
                logTag("PING_ERROR", e.toErrorResponse(errorJsonAdapter).message)
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            }
        }
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)
    }
}