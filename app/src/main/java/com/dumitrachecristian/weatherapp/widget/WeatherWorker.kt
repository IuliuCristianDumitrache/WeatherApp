package com.dumitrachecristian.weatherapp.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dumitrachecristian.weatherapp.repository.WeatherRepository
import com.dumitrachecristian.weatherapp.utils.LocationService
import com.dumitrachecristian.weatherapp.utils.Utils
import com.dumitrachecristian.weatherapp.utils.Utils.Companion.CURRENT_LOCATION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    private val repository: WeatherRepository,
    private val locationService: LocationService,
    private val utils: Utils,
    @Assisted private val appContext : Context,
    @Assisted params : WorkerParameters
): CoroutineWorker(appContext, params){

    override suspend fun doWork(): Result {
        locationService.getCurrentLocation()?.let { location ->
            val result = repository.getCurrentWeather(location.latitude, location.longitude)
            if (result is com.dumitrachecristian.weatherapp.network.Result.Success) {
                result.data?.let { response ->
                    val address = utils.getAddress(location.latitude, location.longitude)
                    val weatherEntity = response.toWeatherEntity().apply {
                        lastUpdated = System.currentTimeMillis()
                        this.addressId = CURRENT_LOCATION
                        this.address = address?.locality ?: address?.subAdminArea ?: ""
                        this.latitude = location.latitude
                        this.longitude = location.longitude
                    }
                    repository.insertWeather(weatherEntity)
                    WeatherWidget().updateAll(appContext)
                }
            }
        }
        return Result.success()
    }

    companion object {
        fun startWorker(context: Context) {
            val request = OneTimeWorkRequestBuilder<WeatherWorker>()
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }

}