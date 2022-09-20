package com.internship.move.model

import com.internship.move.feature.authentication.UserResponse

class OnBoardingRepository(private val userDataInternalStorageManager: UserDataInternalStorageManager) {

    fun getOnboardingStatus() = userDataInternalStorageManager.getOnboardingStatus()

    fun changeLoggedStatus(logValue: Boolean) = userDataInternalStorageManager.setIsUserLoggedIn(logValue)

    fun getAuthStatus(): UserResponse? = userDataInternalStorageManager.getAuthPreferences()
}