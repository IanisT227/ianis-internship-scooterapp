package com.internship.move.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.model.OnBoardingRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repo: OnBoardingRepository) : ViewModel() {

    suspend fun getLoggedStatus() = repo.getLoggedStatus()

    fun changeLogStatus(logValue: Boolean) = viewModelScope.launch { repo.changeLoggedStatus(logValue) }

    suspend fun getAuthStatus(): String? {
        return repo.getAuthStatus()
    }
}