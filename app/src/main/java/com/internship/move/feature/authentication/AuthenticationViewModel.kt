package com.internship.move.feature.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegister
import com.internship.move.utils.ERROR
import com.internship.move.utils.LOGGED
import com.internship.move.utils.UNCHECKED
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val authenticationApi: AuthenticationService) : ViewModel() {

    val onUserLoggedIn: MutableLiveData<Int> = MutableLiveData(UNCHECKED)
    val userData: MutableLiveData<UserResponse> = MutableLiveData(null)

    fun logIn(user: UserLogin) {
        viewModelScope.launch {
            try {
                val response = authenticationApi.loginUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
            } catch (e: Exception) {
                onUserLoggedIn.value = ERROR
            }
        }
    }

    fun register(user: UserRegister) {
        viewModelScope.launch {
            try {
                val response = authenticationApi.registerUser(userdata = user)
                userData.value = response
                onUserLoggedIn.value = LOGGED
            } catch (e: Exception) {
                onUserLoggedIn.value = ERROR
            }
        }
    }

    suspend fun getAll() {
        try {
            val response = authenticationApi.getUsers()
            logTag("LOGIN", response.toString())
        } catch (e: Exception) {
            throw(e)
        }
    }

    companion object {
        private const val SERVER_URL = "https://move-scooter.herokuapp.com/"
    }
}