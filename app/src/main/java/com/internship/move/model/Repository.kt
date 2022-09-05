package com.internship.move.model

class Repository(private val internalStorageManager: InternalStorageManager) {

    suspend fun getLoggedStatus() = internalStorageManager.getLoggedStatus()

    suspend fun changeLoggedStatus(logValue: Boolean) = internalStorageManager.changeLogStatus(logValue)
}