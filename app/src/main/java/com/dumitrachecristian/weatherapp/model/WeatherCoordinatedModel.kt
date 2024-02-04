package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherCoordinatedModel(
    @SerializedName("lat") val latitude: Double? = null,
    @SerializedName("lon") val longitude: Double? = null
)