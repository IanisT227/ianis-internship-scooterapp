package com.internship.move.model

import com.internship.move.feature.authentication.UserResponse

class OnBoardingRepository(private val onBoardingInternalStorageManager: OnBoardingInternalStorageManager) {

    suspend fun getOnboardingStatus() = onBoardingInternalStorageManager.getOnboardingStatus()

    suspend fun changeLoggedStatus(logValue: Boolean) = onBoardingInternalStorageManager.changeLogStatus(logValue)

    suspend fun getAuthStatus(): UserResponse? {
        return onBoardingInternalStorageManager.getAuthPreferences()
    }
}