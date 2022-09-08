package com.internship.move.model

class OnBoardingRepository(private val userDataInternalStorageManager: UserDataInternalStorageManager) {

    suspend fun getLoggedStatus() = userDataInternalStorageManager.getLoggedStatus()

    suspend fun changeLoggedStatus(logValue: Boolean) = userDataInternalStorageManager.changeLogStatus(logValue)

    suspend fun getAuthStatus(): String? {
        return userDataInternalStorageManager.getAuthPreferences()
    }
}