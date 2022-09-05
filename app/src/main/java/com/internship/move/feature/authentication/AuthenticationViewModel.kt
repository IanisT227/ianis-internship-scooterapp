package com.internship.move.feature.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegister
import com.internship.move.utils.logTag
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class AuthenticationViewModel : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val authenticationApi: AuthenticationService =
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthenticationService::class.java)

    suspend fun logIn(user: UserLogin) {
        isLoading.value = true
        try {
            val response = authenticationApi.loginUser(user)
            logTag("LOGIN", response.body().toString())
        } catch (e: Exception) {
            logTag("LOGIN", e.toString())
        } finally {
            isLoading.value = false
        }

    }

    suspend fun register(user: UserRegister) {

    }

    suspend fun getAll() {
        try {
            val response = authenticationApi.getUsers()
            logTag("LOGIN", response.toString())
        } catch (e: Exception) {
            logTag("LOGIN", e.toString())
        }
    }

    companion object {
        private const val SERVER_URL = "https://move-scooter.herokuapp.com/"
    }
}