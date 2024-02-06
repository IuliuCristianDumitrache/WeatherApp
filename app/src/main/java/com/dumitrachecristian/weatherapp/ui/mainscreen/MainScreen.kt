package com.dumitrachecristian.weatherapp.ui.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dumitrachecristian.weatherapp.R
import com.dumitrachecristian.weatherapp.model.uimodel.UiState
import com.dumitrachecristian.weatherapp.navigation.Screen
import com.dumitrachecristian.weatherapp.ui.components.search.SearchBarComponent
import com.dumitrachecristian.weatherapp.ui.components.weather.CurrentWeatherInformationItem
import com.dumitrachecristian.weatherapp.ui.components.weather.CurrentWeatherItem
import com.dumitrachecristian.weatherapp.ui.components.weather.ForecastItem
import com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel.MainViewModel
import com.dumitrachecristian.weatherapp.ui.theme.BlackTransparent
import com.dumitrachecristian.weatherapp.ui.theme.Cloudy
import com.dumitrachecristian.weatherapp.ui.theme.Rainy
import com.dumitrachecristian.weatherapp.ui.theme.Typography
import com.dumitrachecristian.weatherapp.utils.network.ConnectionState
import com.dumitrachecristian.weatherapp.utils.network.connectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController) {
    val permissionGranted by viewModel.permissionGranted.collectAsState()
    val settingsUpdated by viewModel.settingsUpdated.collectAsState()

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    if (!isConnected) {
        viewModel.getDataCurrentLocationDb()
    }
    LaunchedEffect(Unit) {
        viewModel.updateLocations()
    }
    if (settingsUpdated) {
        LaunchedEffect(Unit) {
            viewModel.setSettingsUpdated(false)
            viewModel.getDataCurrentLocation()
        }
    }

    if (permissionGranted) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    SearchBarComponent(viewModel, drawerState, scope)

                    val favoriteItems = viewModel.favoriteLocations.collectAsStateWithLifecycle()
                    LazyColumn() {
                        val modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                        items(favoriteItems.value.size) { index ->
                            FavouriteLocation(modifier, viewModel, favoriteItems.value[index]) {
                                viewModel.setUiState(it)
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        }
                        item() {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    navController.navigate(route = Screen.MapsScreen.route)
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                shape = RoundedCornerShape(size = 10.dp),
                                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Rainy,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = stringResource(R.string.map_location))
                            }

                            Divider(
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                color = Cloudy, thickness = 1.dp
                            )

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    navController.navigate(route = Screen.SettingsScreen.route)
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                shape = RoundedCornerShape(size = 10.dp),
                                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Rainy,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = stringResource(R.string.settings))
                            }
                        }
                    }
                }
            },
            drawerState = drawerState
        ) {
            MainScreenPermissionGranted(
                viewModel = viewModel,
                drawerState = drawerState,
                scope = scope
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.missing_permissions)
            )
        }
    }
}

@Composable
fun MainScreenPermissionGranted(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val weather by viewModel.uiState.collectAsStateWithLifecycle()
    val forecast = viewModel.uiStateForecast

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(weather.mainColor ?: Color.White),
        verticalArrangement = Arrangement.Top,
    ) {

        weather.backgroundImage?.let {
            TopSection(modifier, it, weather, drawerState, scope, viewModel)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CurrentWeatherItem(weather.minTemperature ?: "", stringResource(R.string.min))
            CurrentWeatherItem(weather.temperature ?: "", stringResource(R.string.current))
            CurrentWeatherItem(weather.maxTemperature ?: "", stringResource(R.string.max))
        }

        Divider(color = Color.White, thickness = 1.dp)

        forecast.let { forecasts ->
            LazyColumn {
                items(forecasts.size) { index ->
                    ForecastItem(item = forecasts[index])
                }

                item {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CurrentWeatherInformationItem(
                            Modifier.weight(0.3f),
                            weather.sunrise,
                            R.drawable.ic_sunrise
                        )
                        CurrentWeatherInformationItem(
                            Modifier.weight(0.3f),
                            weather.sunset,
                            R.drawable.ic_sunset
                        )
                        CurrentWeatherInformationItem(
                            Modifier.weight(0.3f),
                            weather.visibility,
                            R.drawable.ic_visibility
                        )
                    }
                    Spacer(modifier.size(20.dp))
                }
            }
        }
    }

}

@Composable
private fun FavouriteLocation(
    modifier: Modifier,
    viewModel: MainViewModel,
    uiState: UiState,
    onClick: (UiState) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(contentColor = Rainy),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .height(110.dp)
            .clickable {
                onClick.invoke(uiState)
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            uiState.backgroundImage?.let {
                val painter = painterResource(it)
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlackTransparent),
            )
            Row {
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (viewModel.isCurrentLocation(uiState.addressId)) {
                            stringResource(R.string.current_location)
                        } else {
                            (uiState.address ?: "")
                        },
                        modifier = Modifier.align(Alignment.Start),
                        color = Color.White
                    )
                    ConstraintLayout(
                        modifier =
                        Modifier.fillMaxWidth()
                    ) {
                        val (weather, temperature) = createRefs()
                        Text(
                            modifier = Modifier
                                .constrainAs(weather) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                }
                                .padding(5.dp),
                            text = uiState.weatherMainDescription ?: "",
                            color = Color.White,
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(temperature) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                }
                                .padding(5.dp),
                            text = uiState.temperature ?: "",
                            color = Color.White,
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )

                    }

                }

                if (!viewModel.isCurrentLocation(uiState.addressId)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Color.White,
                        contentDescription = "Remove icon",
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(16.dp)
                            .clickable {
                                viewModel.removeFromFavorite(uiState.addressId)
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopSection(
    modifier: Modifier,
    it: Int,
    weather: UiState,
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: MainViewModel
) {
    Box(modifier.fillMaxWidth()) {
        val painter = painterResource(it)
        Image(
            modifier = Modifier
                .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                .fillMaxWidth(),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

        IconButton(
            modifier = Modifier
                .padding(top = 40.dp, start = 10.dp),
            onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_content_description),
                tint = Color.White
            )
        }

        Text(
            modifier = Modifier
                .padding(bottom = 10.dp, end = 10.dp)
                .align(Alignment.BottomEnd),
            text = stringResource(R.string.last_updated, viewModel.getLastUpdatedTime(weather.time)),
            color = Color.White,
            style = Typography.labelSmall
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .offset(y = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.temperature ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                style = Typography.headlineLarge
            )
            Text(
                text = weather.feelsLike ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                style = Typography.titleLarge
            )
            Text(
                text = weather.weatherMainDescription?.uppercase() ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                style = Typography.headlineMedium
            )
            Text(
                text = weather.address ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                style = Typography.titleLarge
            )
        }
    }
}