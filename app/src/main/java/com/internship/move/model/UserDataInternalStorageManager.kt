package com.internship.move.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.internship.move.feature.authentication.UserResponse
import com.squareup.moshi.Moshi

class UserDataInternalStorageManager(context: Context, private val moshi: Moshi) {

    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

    fun getOnboardingStatus(): Boolean {
        return preferences.getBoolean(KEY_PASSED_ONBOARDING, false)
    }

    fun changeLogStatus(logValue: Boolean) {
        preferences.edit().putBoolean(KEY_PASSED_ONBOARDING, logValue).apply()
    }

    fun getAuthPreferences(): UserResponse? {
        return if (preferences.getString(KEY_IS_AUTH, "").isNullOrEmpty() || preferences.getString(KEY_IS_AUTH, "").equals("null")) {
            null
        } else {
            val userJsonAdapter = moshi.adapter(UserResponse::class.java)
            userJsonAdapter.fromJson(preferences.getString(KEY_IS_AUTH, ""))
        }
    }

    fun uploadLicensePicture(filepath: String) {
        val response: UserResponse? = getAuthPreferences()
        response?.user?.driverLicenseKey = filepath
        changeAuthPreferences(userData = response)
    }

    fun changeAuthPreferences(userData: UserResponse?) {
        val userJsonAdapter = moshi.adapter(UserResponse::class.java)
        val userStringData = userJsonAdapter.toJson(userData)
        preferences.edit().putString(KEY_IS_AUTH, userStringData).apply()
    }

    fun logOutUser() {
        preferences.edit().putString(KEY_IS_AUTH, "null").apply()
    }

    companion object {
        private const val KEY_PREFERENCES = "com.internship.move.KEY_PREFERENCES"
        private const val KEY_PASSED_ONBOARDING = "IS_LOGGED"
        private const val KEY_IS_AUTH = "IS_AUTH"
    }
}