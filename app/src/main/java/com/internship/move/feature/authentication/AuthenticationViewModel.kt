package com.internship.move.feature.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegisterRequest
import com.internship.move.model.UserDataInternalStorageManager
import com.internship.move.utils.ERROR
import com.internship.move.utils.LOGGED
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authenticationApi: AuthenticationService,
    private val onBoardingInternalStorageManager: UserDataInternalStorageManager
) : ViewModel() {

    val onUserLoggedIn: MutableLiveData<Int> = MutableLiveData()
    val userData: MutableLiveData<UserResponse> = MutableLiveData(null)
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun logIn(user: UserLogin) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = authenticationApi.loginUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
                onBoardingInternalStorageManager.changeAuthPreferences(userData.value)
            } catch (e: Exception) {
                onUserLoggedIn.value = ERROR
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(user: UserRegisterRequest) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = authenticationApi.registerUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
                onBoardingInternalStorageManager.changeAuthPreferences(userData.value)
            } catch (e: Exception) {
                logTag("REGISTER", e.toString())
                onUserLoggedIn.value = ERROR
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            try {
                logTag("LOGOUT", userData.value?.token.toString())
                authenticationApi.logoutUser()
            } catch (e: Exception) {
                logTag("LOGOUT", e.toString())
            } finally {
                onBoardingInternalStorageManager.logOutUser()
            }

        }
    }

    fun getUserToken() = userData.value?.token

    fun getCurrentUser() {
        viewModelScope.launch {
            userData.value = onBoardingInternalStorageManager.getAuthPreferences()
        }
    }
}