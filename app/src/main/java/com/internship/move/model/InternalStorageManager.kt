package com.internship.move.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class InternalStorageManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(KEY_IS_LOGGED, MODE_PRIVATE)

    fun getLoggedStatus(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED, false)
    }

    fun changeLogStatus(logValue: Boolean) {
        preferences.edit().putBoolean(KEY_IS_LOGGED, logValue).apply()
    }

    companion object {
        private const val KEY_IS_LOGGED = "IS_LOGGED"
    }
}