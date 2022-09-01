package com.internship.move.model

import com.internship.move.InternalStorageManager

class Repository(private val internalStorageManager: InternalStorageManager) {

    suspend fun logIn() {
        internalStorageManager.logIn()
    }

    suspend fun logOut() {
        internalStorageManager.logOut()
    }

    suspend fun getLoggedStatus() = internalStorageManager.getLoggedStatus()
}