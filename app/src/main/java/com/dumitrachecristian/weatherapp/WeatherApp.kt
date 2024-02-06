package com.dumitrachecristian.weatherapp

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.dumitrachecristian.weatherapp.data.DatabaseHelper
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherApp : Application(), Configuration.Provider {
    companion object {
        lateinit var appContext: Context
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        DatabaseHelper.initDB(applicationContext)
        appContext = applicationContext

        val apiKey = BuildConfig.PLACES_API_KEY
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}