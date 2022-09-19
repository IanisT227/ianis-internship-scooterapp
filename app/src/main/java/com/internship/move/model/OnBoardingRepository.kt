package com.internship.move.model

import com.internship.move.feature.authentication.UserResponse

class OnBoardingRepository(private val userDataInternalStorageManager: UserDataInternalStorageManager) {

    suspend fun getOnboardingStatus() = userDataInternalStorageManager.getOnboardingStatus()

    suspend fun changeLoggedStatus(logValue: Boolean) = userDataInternalStorageManager.changeLogStatus(logValue)

    suspend fun getAuthStatus(): UserResponse? {
        return userDataInternalStorageManager.getAuthPreferences()
    }
}