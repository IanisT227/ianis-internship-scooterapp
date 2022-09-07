package com.internship.move.feature.onboarding

import androidx.lifecycle.ViewModel
import com.internship.move.model.OnBoardingRepository

class OnboardingViewModel(private val repo: OnBoardingRepository) : ViewModel() {

    suspend fun getLoggedStatus() = repo.getLoggedStatus()

    suspend fun changeLogStatus(logValue: Boolean) = repo.changeLoggedStatus(logValue)
}