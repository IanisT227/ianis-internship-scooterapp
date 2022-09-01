package com.internship.move

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class InternalStorageManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_IS_LOGGED, MODE_PRIVATE)

    fun logIn() {
        preferences.edit().putBoolean(KEY_IS_LOGGED, true).apply()
    }

    fun logOut() {
        preferences.edit().putBoolean(KEY_IS_LOGGED, false).apply()
    }

    fun getLoggedStatus(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED, false)
    }

    companion object {
        private const val KEY_IS_LOGGED = "IS_LOGGED"
    }
}