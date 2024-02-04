package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("weather") val weather: List<Weather>? = null,
    @SerializedName("dt") val time: Long? = null,
    @SerializedName("temp") val temperature: Temperature? = null
)
