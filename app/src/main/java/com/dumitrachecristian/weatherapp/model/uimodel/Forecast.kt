package com.dumitrachecristian.weatherapp.model.uimodel

data class Forecast(
    val dateMillis: Long? = null,
    val day: String,
    val icon: String? = null,
    val temperature: String? = null,
)