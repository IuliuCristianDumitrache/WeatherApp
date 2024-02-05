package com.dumitrachecristian.weatherapp.model

import com.dumitrachecristian.weatherapp.data.WeatherEntity
import com.google.gson.annotations.SerializedName

data class WeatherModelResponse(
    // NOTE: It is possible to meet more than one weather condition for a requested location. The first weather condition in API respond is primary
    @SerializedName("weather") val weatherConditions: List<WeatherCondition>? = null,
    @SerializedName("main") val weatherMain: WeatherMain? = null,
    @SerializedName("visibility") val visibility: Int? = null,
    @SerializedName("wind") val wind: Wind? = null,
    @SerializedName("dt") val time: Long? = null,
    @SerializedName("sys") val sys: Sys? = null,
    @Transient val lastUpdated: Long? = null,
    @Transient val address: String? = null,
    @Transient val addressId: String? = null,
    @Transient val latitude: Double? = null,
    @Transient val longitude: Double? = null
) {

    fun toWeatherEntity(): WeatherEntity {
        return WeatherEntity(
            time = time ?: 0,
            sunset = sys?.sunset ?: 0,
            sunrise = sys?.sunrise ?: 0,
            visibility = visibility ?: 0,
            temperature = weatherMain?.temperature ?: 0.0,
            minTemperature = weatherMain?.minTemperature ?: 0.0,
            maxTemperature = weatherMain?.maxTemperature ?: 0.0,
            feelsLike = weatherMain?.feelsLike ?: 0.0,
            conditionId = getPrimaryWeatherCondition()?.id ?: 0,
            conditionName = getPrimaryWeatherCondition()?.main ?: "",
            conditionIcon = getPrimaryWeatherCondition()?.icon ?: ""
        )
    }
    fun getPrimaryWeatherCondition(): WeatherCondition? {
        return if (weatherConditions?.isNotEmpty() == true) {
            weatherConditions[0]
        } else {
            null
        }
    }
}