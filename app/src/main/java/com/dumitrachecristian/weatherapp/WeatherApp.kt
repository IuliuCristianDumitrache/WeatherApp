package com.dumitrachecristian.weatherapp

import android.app.Application
import android.content.Context
import com.dumitrachecristian.weatherapp.data.DatabaseHelper.initDB
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
     //   initDB(applicationContext)
        appContext = applicationContext
    }
}