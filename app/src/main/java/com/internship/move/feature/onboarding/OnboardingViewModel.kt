package com.internship.move.feature.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.feature.authentication.UserResponse
import com.internship.move.model.OnBoardingRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repo: OnBoardingRepository) : ViewModel() {

    val userLoggedStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val userData: MutableLiveData<UserResponse?> = MutableLiveData(null)

    fun getOnboardingStatus() = viewModelScope.launch {
        userLoggedStatus.value = repo.getOnboardingStatus()
    }

    fun changeLogStatus(logValue: Boolean) = viewModelScope.launch { repo.changeLoggedStatus(logValue) }

    fun getAuthData() {
        viewModelScope.launch {
            userData.value = repo.getAuthStatus()
        }
    }
}