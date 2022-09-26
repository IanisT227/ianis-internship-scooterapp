package com.internship.move.feature.scooter_unlock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                logTag("SCOOTER_ID", _scooterResult.value?.id.toString())
                scooterStateService.lockScooter(_scooterResult.value?.scooterNumber?.toInt() ?: 1000)
                _scooterResult.value = null
                _isError.value = null
            } catch (e: Exception) {
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            }
        }
    }
}