package com.dumitrachecristian.weatherapp

import android.app.Application
import android.content.Context
import com.dumitrachecristian.weatherapp.data.DatabaseHelper
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        DatabaseHelper.initDB(applicationContext)
        appContext = applicationContext

        val apiKey = BuildConfig.PLACES_API_KEY
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)
    }
}