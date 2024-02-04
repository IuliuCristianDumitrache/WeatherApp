package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class ForecastModelResponse(
    @SerializedName("list") val list: List<WeatherModelResponse>? = null
)