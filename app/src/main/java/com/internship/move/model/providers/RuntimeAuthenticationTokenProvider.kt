package com.internship.move.model.providers

import com.internship.move.model.UserDataInternalStorageManager

class RuntimeAuthenticationTokenProvider(private val internalStorageManager: UserDataInternalStorageManager) : AuthenticationTokenProvider {
    override fun getAuthToken() = internalStorageManager.getUserToken()
}