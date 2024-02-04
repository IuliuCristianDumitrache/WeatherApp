package com.dumitrachecristian.weatherapp

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumitrachecristian.weatherapp.network.Result
import com.dumitrachecristian.weatherapp.repository.WeatherRepository
import com.dumitrachecristian.weatherapp.utils.LocationService
import com.dumitrachecristian.weatherapp.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    val forecast: ArrayList<Forecast>? = null
)

data class Forecast(
    val day: String,
    val icon: String? = null,
    val temperature: String? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationService: LocationService,
    private val utils: Utils
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    private fun getData() {
        viewModelScope.launch {
            locationService.getCurrentLocation()?.let { location ->
                getCurrentWeather(location.latitude, location.longitude)
                getForecast(location.latitude, location.longitude)
            }
        }
    }

    private fun getForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when (val result = repository.getForecast(latitude, longitude)) {
                is Result.Success -> {
                    result.data?.let { response ->
                        val forecastList = arrayListOf<Forecast>()
                        response.list?.forEach { weather ->
                            forecastList.add(
                                Forecast(
                                    day = utils.getDayFromTimeMillis(weather.time),
                                    icon = utils.getIconUrl(if (weather.weather?.isNotEmpty() == true) weather.weather[0].icon else null),
                                    temperature = utils.formatTemperature(weather.weatherMain?.temperature?.toInt())
                                )
                            )
                        }

                        _uiState.value = _uiState.value.copy(
                            forecast = forecastList
                        )
                    }
                }

                is Result.Error -> {

                }

                is Result.Loading -> {

                }
            }
        }
    }

    fun getCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when (val result = repository.getCurrentWeather(latitude, longitude)) {
                is Result.Success -> {
                    result.data?.let { response ->
                        val primaryWeather = if (response.weather?.isNotEmpty() == true) response.weather[0] else null
                        val address = utils.getAddress(latitude, longitude)
                        _uiState.value = _uiState.value.copy(
                            temperature = utils.formatTemperature(response.weatherMain?.temperature?.toInt()),
                            feelsLike = utils.formatFeelsLikeTemperature(response.weatherMain?.feelsLike?.toInt()),
                            weatherMainDescription = primaryWeather?.main,
                            backgroundImage = utils.getBackgroundImage(primaryWeather?.id),
                            mainColor = utils.getMainColor(primaryWeather?.id),
                            minTemperature = utils.formatTemperature(response.weatherMain?.minTemperature?.toInt()),
                            maxTemperature = utils.formatTemperature(response.weatherMain?.maxTemperature?.toInt()),
                            sunset = utils.getTimeFromTimeMillis(response.sys?.sunset),
                            sunrise = utils.getTimeFromTimeMillis(response.sys?.sunrise),
                            visibility = utils.formatVisibilityInKm(response.visibility),
                            address = address?.locality ?: address?.subAdminArea
                        )
                    }
                }
                is Result.Error -> {

                }
                is Result.Loading -> {

                }
            }

        }
    }

    fun setPermissionGranted(permissionGranted: Boolean) {
        _permissionGranted.value = permissionGranted
        if (permissionGranted) {
            getData()
        }
    }
}