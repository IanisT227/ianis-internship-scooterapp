package com.internship.move.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.internship.move.feature.authentication.User
import com.internship.move.feature.authentication.UserResponse

class OnBoardingInternalStorageManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

    fun getLoggedStatus(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED, false)
    }

    fun changeLogStatus(logValue: Boolean) {
        preferences.edit().putBoolean(KEY_IS_LOGGED, logValue).apply()
    }

    fun getAuthPreferences(): String? {
        return preferences.getString(KEY_IS_AUTH, "")
    }

    fun changeAuthPreferences(userData: UserResponse?) {
        val userStringData: String = Gson().toJson(userData)
        preferences.edit().putString(KEY_IS_AUTH, userStringData).apply()
    }

    companion object {
        private const val KEY_PREFERENCES = "com.internship.move.KEY_PREFERENCES"
        private const val KEY_IS_LOGGED = "IS_LOGGED"
        private const val KEY_IS_AUTH = "IS_AUTH"
    }
}