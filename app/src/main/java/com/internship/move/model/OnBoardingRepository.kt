package com.internship.move.model

class OnBoardingRepository(private val onBoardingInternalStorageManager: OnBoardingInternalStorageManager) {

    suspend fun getLoggedStatus() = onBoardingInternalStorageManager.getLoggedStatus()

    suspend fun changeLoggedStatus(logValue: Boolean) = onBoardingInternalStorageManager.changeLogStatus(logValue)
}