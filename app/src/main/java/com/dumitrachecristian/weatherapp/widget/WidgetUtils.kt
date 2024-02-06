package com.dumitrachecristian.weatherapp.widget

import android.content.Context
import com.dumitrachecristian.weatherapp.repository.WeatherRepository
import com.dumitrachecristian.weatherapp.utils.Utils
import dagger.hilt.android.EntryPointAccessors

object WidgetUtils {
    fun getWeatherRepository(context: Context): WeatherRepository {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.weatherRepository()
    }

    fun getUtils(context: Context): Utils {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.utils()
    }
}