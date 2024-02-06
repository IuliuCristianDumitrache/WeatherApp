package com.dumitrachecristian.weatherapp.widget

import com.dumitrachecristian.weatherapp.repository.WeatherRepository
import com.dumitrachecristian.weatherapp.utils.Utils
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun weatherRepository(): WeatherRepository
    fun utils(): Utils
}