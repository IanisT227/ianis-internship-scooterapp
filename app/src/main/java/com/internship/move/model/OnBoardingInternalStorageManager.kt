package com.internship.move.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.internship.move.feature.authentication.UserResponse

class OnBoardingInternalStorageManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

    fun getOnboardingStatus(): Boolean {
        return preferences.getBoolean(KEY_PASSED_ONBOARDING, false)
    }

    fun changeLogStatus(logValue: Boolean) {
        preferences.edit().putBoolean(KEY_PASSED_ONBOARDING, logValue).apply()
    }

    fun getAuthPreferences(): UserResponse? {
        return if (preferences.getString(KEY_IS_AUTH, "").isNullOrEmpty() || preferences.getString(KEY_IS_AUTH, "").equals(NULL_USER))
            null
        else
            Gson().fromJson(preferences.getString(KEY_IS_AUTH, ""), UserResponse::class.java)
    }

    fun changeAuthPreferences(userData: UserResponse?) {
        val userStringData: String = Gson().toJson(userData)
        preferences.edit().putString(KEY_IS_AUTH, userStringData).apply()
    }

    companion object {
        private const val KEY_PREFERENCES = "com.internship.move.KEY_PREFERENCES"
        private const val KEY_PASSED_ONBOARDING = "IS_LOGGED"
        private const val KEY_IS_AUTH = "IS_AUTH"
        private const val NULL_USER =
            "{\"token\":\"\",\"user\":{\"driverLicenseKey\":\"\",\"email\":\"\",\"id\":\"\",\"status\":\"\",\"username\":\"\"}}"
    }
}