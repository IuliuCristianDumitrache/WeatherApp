package com.dumitrachecristian.weatherapp.model.uimodel

import androidx.compose.ui.graphics.Color

data class UiState(
    val temperature: String? = null,
    val feelsLike: String? = null,
    val weatherMainDescription: String? = null,
    val backgroundImage: Int? = null,
    val mainColor: Color? = null,
    val minTemperature: String? = null,
    val maxTemperature: String? = null,
    val sunrise: String? = null,
    val sunset: String? = null,
    val visibility: String? = null,
    val address: String? = null,
    val addressId: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
)