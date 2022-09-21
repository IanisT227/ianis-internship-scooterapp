package com.internship.move.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.internship.move.feature.authentication.UserResponse
import com.squareup.moshi.Moshi

class UserDataInternalStorageManager(context: Context, private val moshi: Moshi) {

    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

    fun getOnboardingStatus(): Boolean = preferences.getBoolean(KEY_PASSED_ONBOARDING, false)

    fun setIsUserLoggedIn(isLoggedIn: Boolean) = preferences.edit().putBoolean(KEY_PASSED_ONBOARDING, isLoggedIn).apply()

    fun getAuthPreferences(): UserResponse? {
        val user = preferences.getString(KEY_IS_AUTH, null)
        return if (user.isNullOrEmpty()) {
            null
        } else {
            val userJsonAdapter = moshi.adapter(UserResponse::class.java)
            userJsonAdapter.fromJson(user)
        }
    }

    fun uploadLicensePicture(filepath: String) {
        val response: UserResponse? = getAuthPreferences()
        response?.userDTO?.driverLicenseKey = filepath
        changeAuthPreferences(userData = response)
    }

    fun changeAuthPreferences(userData: UserResponse?) {
        val userJsonAdapter = moshi.adapter(UserResponse::class.java)
        val userStringData = userJsonAdapter.toJson(userData)
        preferences.edit().putString(KEY_IS_AUTH, userStringData).apply()
    }

    fun logOutUser() = preferences.edit().putString(KEY_IS_AUTH, null).apply()

    fun getUserToken(): String? = getAuthPreferences()?.token

    companion object {
        private const val KEY_PREFERENCES = "com.internship.move.KEY_PREFERENCES"
        private const val KEY_PASSED_ONBOARDING = "KEY_PASSED_ONBOARDING"
        private const val KEY_IS_AUTH = "KEY_IS_AUTH"
    }
}