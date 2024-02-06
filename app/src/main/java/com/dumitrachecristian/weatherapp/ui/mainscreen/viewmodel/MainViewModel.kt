package com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumitrachecristian.weatherapp.data.SessionManager
import com.dumitrachecristian.weatherapp.data.WeatherEntity
import com.dumitrachecristian.weatherapp.data.toWeatherModelResponse
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.model.uimodel.Forecast
import com.dumitrachecristian.weatherapp.model.uimodel.MenuItem
import com.dumitrachecristian.weatherapp.model.uimodel.SearchResult
import com.dumitrachecristian.weatherapp.model.uimodel.UiState
import com.dumitrachecristian.weatherapp.network.Result
import com.dumitrachecristian.weatherapp.repository.WeatherRepository
import com.dumitrachecristian.weatherapp.utils.LocationService
import com.dumitrachecristian.weatherapp.utils.PlacesService
import com.dumitrachecristian.weatherapp.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationService: LocationService,
    private val utils: Utils,
    private val placesService: PlacesService,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var _uiStateForecast = mutableStateListOf<Forecast>()
    val uiStateForecast: List<Forecast> = _uiStateForecast

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    private var _searchResult = mutableStateListOf<SearchResult>()
    val searchResult: List<SearchResult> = _searchResult

    private val _settingsUpdated = MutableStateFlow(false)
    val settingsUpdated: StateFlow<Boolean> = _settingsUpdated.asStateFlow()

    val favoriteLocations: StateFlow<List<UiState>> = repository
        .getFavoriteLocations()
        .transform { weatherEntityList ->
            val newList = arrayListOf<WeatherModelResponse>()
            weatherEntityList.forEach {
                newList.add(it.toWeatherModelResponse())
            }
            emit(newList)
        }
        .map { weatherModelResponseList ->
            val list = arrayListOf<UiState>()
            weatherModelResponseList.forEach { weatherModelResponse ->
                list.add(parseToUiState(weatherModelResponse, weatherModelResponse.address))
            }
            list.partition { it.addressId == CURRENT_LOCATION }.toList().flatten()
        }
        .catch { exception -> exception.localizedMessage?.let { Log.e("MainViewModel", it) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun getDataCurrentLocation() {
        viewModelScope.launch {
            locationService.getCurrentLocation()?.let { location ->
                getCurrentWeather(location.latitude, location.longitude)
                getForecast(location.latitude, location.longitude)
            }
        }
    }

    fun getDataForLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            getCurrentWeather(latitude, longitude)
            getForecast(latitude, longitude)
        }
    }

    private fun getForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when (val result = repository.getForecast(latitude, longitude)) {
                is Result.Success -> {
                    result.data?.let { response ->
                        val forecastList = utils.formatForecastMedian(response.list)

                        forecastList?.let {
                            _uiStateForecast.clear()
                            _uiStateForecast.addAll(it)
                        }
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
                        val address = utils.getAddress(latitude, longitude)
                        _uiState.value = parseToUiState(response, address?.locality ?: address?.subAdminArea)

                        val weatherEntity = response.toWeatherEntity().apply {
                            lastUpdated = System.currentTimeMillis()
                            addressId = CURRENT_LOCATION
                            this.address = address?.locality ?: address?.subAdminArea ?: ""
                            this.latitude = latitude
                            this.longitude = longitude
                        }
                        weatherEntity.let {
                            repository.insertWeather(weatherEntity)
                        }
                    }
                }
                is Result.Error -> {

                }
                is Result.Loading -> {

                }
            }

        }
    }

    private fun parseToUiState(
        response: WeatherModelResponse,
        address: String?
    ): UiState {
        val primaryWeather = response.getPrimaryWeatherCondition()

        return UiState(
            temperature = utils.formatTemperature(response.weatherMain?.temperature?.toInt()),
            feelsLike = utils.formatFeelsLikeTemperature(response.weatherMain?.feelsLike?.toInt()),
            weatherMainDescription = primaryWeather?.main,
            backgroundImage = utils.getBackgroundImage(primaryWeather?.id),
            mainColor = utils.getMainColor(primaryWeather?.id),
            minTemperature = utils.formatTemperature(response.weatherMain?.minTemperature?.toInt()),
            maxTemperature = utils.formatTemperature(response.weatherMain?.maxTemperature?.toInt()),
            sunset = utils.getTimeFromTimeSeconds(response.sys?.sunset),
            sunrise = utils.getTimeFromTimeSeconds(response.sys?.sunrise),
            visibility = utils.formatVisibilityInKm(response.visibility),
            address = address,
            addressId = response.addressId,
            latitude = response.latitude,
            longitude = response.longitude
        )
    }

    fun setPermissionGranted(permissionGranted: Boolean) {
        _permissionGranted.value = permissionGranted
        if (permissionGranted) {
            getDataCurrentLocation()
        }
    }

    fun searchLocation(searchText: String) {
        viewModelScope.launch {
            val places = placesService.searchLocation(searchText)

            val searchResultList = arrayListOf<SearchResult>()
            places?.forEach {
                var address: WeatherEntity? = null
                it.id?.let {addressId ->
                    address = repository.getWeatherByAddressId(addressId)
                }
                searchResultList.add(
                    SearchResult(
                        address = it.address ?: "",
                        id = it.id ?: "",
                        latitude = it.latLng?.latitude ?: 0.0,
                        longitude = it.latLng?.longitude ?: 0.0,
                        isFavorite = address != null
                    )
                )
            }
            _searchResult.clear()
            _searchResult.addAll(searchResultList)
        }
    }

    fun setSettingsUpdated(value: Boolean) {
        _settingsUpdated.value = value
    }

    fun updateUnit(menuItem: MenuItem) {
        sessionManager.setUnit(menuItem.value)
        setSettingsUpdated(true)
    }

    fun getUnit(): String {
        return sessionManager.getUnit()
    }

    fun addRemoveFromFavorites(searchResult: SearchResult) {
        viewModelScope.launch {
            if (searchResult.isFavorite) {
                repository.removeWeather(searchResult.id)
                val indexToUpdate = _searchResult.indexOfFirst { it.id == searchResult.id }
                _searchResult[indexToUpdate] = _searchResult[indexToUpdate].copy(isFavorite = false)
            } else {
                when (val currentWeather = repository.getCurrentWeather(searchResult.latitude, searchResult.longitude)) {
                    is Result.Success -> {
                        val weatherEntity = currentWeather.data?.toWeatherEntity()?.apply {
                            lastUpdated = System.currentTimeMillis()
                            addressId = searchResult.id
                            address = searchResult.address
                            latitude = searchResult.latitude
                            longitude = searchResult.longitude

                        }
                        weatherEntity?.let {
                            repository.insertWeather(weatherEntity)
                        }

                        val indexToUpdate = _searchResult.indexOfFirst { it.id == searchResult.id }
                        _searchResult[indexToUpdate] = _searchResult[indexToUpdate].copy(isFavorite = true)
                    }
                    is Result.Error -> {}
                    is Result.Loading -> {}
                }
            }
        }
    }

    fun setUiState(uiState: UiState) {
        _uiState.value = uiState
        _uiStateForecast.clear()
        uiState.latitude?.let { latitude ->
            uiState.longitude?.let { longitude ->
                getForecast(latitude, longitude)
            }
        }
    }

    fun removeFromFavorite(addressId: String?) {
        viewModelScope.launch {
            addressId?.let {
                repository.removeWeather(addressId)
            }
        }
    }

    fun updateLocations() {
        viewModelScope.launch {
            val weatherEntityList = repository.getFavoriteLocationsSuspend()
            weatherEntityList.forEach { weatherEntity ->
                when (val result = repository.getCurrentWeather(weatherEntity.latitude, weatherEntity.longitude)) {
                    is Result.Success -> {
                        result.data?.let { weather ->
                            val newWeatherEntity = weather.toWeatherEntity().apply {
                                lastUpdated = System.currentTimeMillis()
                                addressId = weatherEntity.addressId
                                address = weatherEntity.address
                                latitude = weatherEntity.latitude
                                longitude = weatherEntity.longitude

                            }
                            repository.insertWeather(newWeatherEntity)
                        }
                    }

                    is Result.Error -> {

                    }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }

    fun isCurrentLocation(addressId: String?): Boolean = addressId == CURRENT_LOCATION
    fun getDataCurrentLocationDb() {
        viewModelScope.launch {
            val entity = repository.getWeatherByAddressId(CURRENT_LOCATION)
            _uiState.value = parseToUiState(entity.toWeatherModelResponse(), entity.address)
        }
    }

    companion object {
        const val CURRENT_LOCATION = "CURRENT_LOCATION"
    }
}