package com.dumitrachecristian.weatherapp.model.uimodel

data class SearchResult(
    val address: String,
    val id: String,
    val latitude: Double,
    val longitude: Double,
    var isFavorite: Boolean = false
)