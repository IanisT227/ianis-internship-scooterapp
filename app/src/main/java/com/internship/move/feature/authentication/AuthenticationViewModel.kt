package com.internship.move.feature.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegisterRequest
import com.internship.move.model.UserDataInternalStorageManager
import com.internship.move.utils.ERROR
import com.internship.move.utils.LOGGED
import com.internship.move.utils.UNCHECKED
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authenticationApi: AuthenticationService,
    private val onBoardingInternalStorageManager: UserDataInternalStorageManager
) : ViewModel() {

    val onUserLoggedIn: MutableLiveData<Int> = MutableLiveData(UNCHECKED)
    val userData: MutableLiveData<UserResponse> = MutableLiveData(null)
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    fun logIn(user: UserLogin) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = authenticationApi.loginUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
                onBoardingInternalStorageManager.changeAuthPreferences(userData.value)
            } catch (e: Exception) {
                onUserLoggedIn.value = ERROR
            } finally {
                isLoading.value = false
            }
        }
    }

    fun register(user: UserRegisterRequest) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = authenticationApi.registerUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
                onBoardingInternalStorageManager.changeAuthPreferences(userData.value)
            } catch (e: Exception) {
                logTag("REGISTER", e.toString())
                onUserLoggedIn.value = ERROR
            } finally {
                isLoading.value = false
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            try {
                logTag("LOGOUT", userData.value?.token.toString())
                authenticationApi.logoutUser("Bearer " + userData.value?.token)
                onBoardingInternalStorageManager.logOutUser()
            } catch (e: Exception) {
                logTag("LOGOUT", e.toString())
            }

        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            userData.value = onBoardingInternalStorageManager.getAuthPreferences()
        }
    }
}