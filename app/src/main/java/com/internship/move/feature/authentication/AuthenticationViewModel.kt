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

    fun logIn(user: UserLogin) {
        viewModelScope.launch {
            try {
                val response = authenticationApi.loginUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
                onBoardingInternalStorageManager.changeAuthPreferences(userData.value!!)
            } catch (e: Exception) {
                onUserLoggedIn.value = ERROR
            }
        }
    }

    fun register(user: UserRegisterRequest) {
        viewModelScope.launch {
            try {
                val response = authenticationApi.registerUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
            } catch (e: Exception) {
                logTag("REGISTER", e.toString())
                onUserLoggedIn.value = ERROR
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            try {
                logTag("LOGOUT", userData.value?.token.toString())
                authenticationApi.logoutUser("Bearer " + userData.value?.token)
                onBoardingInternalStorageManager.changeAuthPreferences(userData = UserResponse("", User("", "", "", "", "")))
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

    companion object {
        private const val SERVER_URL = "https://move-scooter.herokuapp.com/"
    }
}