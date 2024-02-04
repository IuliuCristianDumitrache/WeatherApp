package com.dumitrachecristian.weatherapp.data

import android.content.SharedPreferences

class SessionManager(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val UNIT_KEY = "UNIT_KEY"
    }

    fun setUnit(unit: String) {
        sharedPreferences.edit().putString(UNIT_KEY, unit).apply()
    }

    fun getUnit(): String {
        return sharedPreferences.getString(UNIT_KEY, "metric")!!
    }
}