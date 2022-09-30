package com.internship.move.feature.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.feature.menu.rideHistory.PageDataDTO
import com.internship.move.feature.menu.rideHistory.RideHistoryItemDTO
import com.internship.move.model.ErrorResponse
import com.internship.move.utils.logTag
import com.internship.move.utils.toErrorResponse
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.launch

class MenuViewModel(
    private val mainMenuService: MainMenuService,
    private val errorJsonAdapter: JsonAdapter<ErrorResponse>
) : ViewModel() {

    private val _userData: MutableLiveData<MainMenuUserDTO> = MutableLiveData()
    val userData: LiveData<MainMenuUserDTO>
        get() = _userData
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isError: MutableLiveData<String?> = MutableLiveData()
    val isError: LiveData<String?>
        get() = _isError

    private val _ridesList: MutableLiveData<List<RideHistoryItemDTO>> = MutableLiveData()
    val ridesList: LiveData<List<RideHistoryItemDTO>>
        get() = _ridesList

    fun getUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _userData.value = mainMenuService.getUserByToken()
            } catch (e: Exception) {
                logTag("ENDRIDE_RESET", e.toErrorResponse(errorJsonAdapter).message)
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserRides() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _ridesList.value = mainMenuService.getRidesByUser(pageSize = 8)
            } catch (e: Exception) {
                logTag("ENDRIDE_RESET", e.toErrorResponse(errorJsonAdapter).message)
                _isError.postValue(e.toErrorResponse(errorJsonAdapter).message)

            } finally {
                _isLoading.value = false
            }
        }
    }
}