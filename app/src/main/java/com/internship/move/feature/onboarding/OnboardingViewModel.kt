package com.internship.move.feature.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {

    val isLogged: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun logIn() {
        isLogged.value = true
    }

    suspend fun logOut() {
        isLogged.value = false
    }
}