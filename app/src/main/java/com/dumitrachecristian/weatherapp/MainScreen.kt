package com.dumitrachecristian.weatherapp

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dumitrachecristian.weatherapp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController) {
    val permissionGranted by viewModel.permissionGranted.collectAsState()
    if (permissionGranted) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    SearchBarComponent(drawerState, scope)

                    LazyColumn() {
                        val modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                        items(10) {
                            FavouriteLocation(modifier)
                        }
                        item() {

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    navController.navigate(route = Screen.ManageLocationsScreen.route)
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                shape = RoundedCornerShape(size = 10.dp),
                                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(text = "Manage Locations")
                            }

                            Divider(color = Color.Black, thickness = 1.dp)

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
                                    containerColor = Color.LightGray,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(text = "Settings")
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
        Box {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.missing_permissions)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(drawerState: DrawerState, scope: CoroutineScope) {
    var searchText by remember { mutableStateOf("") }
    val searchActive = remember { mutableStateOf(false) }

    SearchBar(modifier = Modifier
        .padding(top = 18.dp)
        .fillMaxWidth()
        .padding(horizontal = if (searchActive.value) 0.dp else 14.dp),
        tonalElevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        colors = SearchBarDefaults.colors(
            containerColor = Color.LightGray, dividerColor = Color.Black,
            inputFieldColors = SearchBarDefaults.inputFieldColors()
        ),
        query = searchText,
        onQueryChange = {
            searchText = it
        },
        onSearch = {
            searchActive.value = false
            scope.launch {
                drawerState.close()
            }
        },
        active = searchActive.value,
        onActiveChange = {
            searchActive.value = it
        },
        placeholder = {
            Text(text = "Search for a city")
        },
        leadingIcon = {
            if (searchActive.value) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back icon",
                    modifier = Modifier
                        .clickable {
                            searchActive.value = false
                        })
            } else {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu icon")
            }
        },
        trailingIcon = {
            if (searchActive.value) {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    modifier = Modifier.clickable {
                        searchActive.value = false
                        scope.launch {
                            drawerState.close()
                        }
                    })
            }
        }) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenPermissionGranted(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val weather by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(weather.mainColor ?: Color.White),
        verticalArrangement = Arrangement.Top,
    ) {

        weather.backgroundImage?.let {
            TopSection(modifier, it, weather, drawerState, scope)
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

        weather.forecast?.let { forecasts ->
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
fun CurrentWeatherInformationItem(
    modifier: Modifier = Modifier,
    value: String?,
    @DrawableRes drawableRes: Int
) {
    Column(modifier = modifier) {
        Icon(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(drawableRes),
            contentDescription = null,
            tint = Color.White
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = value ?: "",
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

@Composable
fun ForecastItem(item: Forecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.day,
            color = Color.White,
            fontSize = 20.sp
        )
        AsyncImage(
            modifier = Modifier.size(30.dp),
            model = item.icon,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = item.temperature ?: "",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun CurrentWeatherItem(temperature: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = temperature,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = label,
            color = Color.White
        )
    }
}

@Composable
private fun FavouriteLocation(modifier: Modifier) {
    Card(
        colors = CardDefaults.cardColors(contentColor = Color.Blue),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today",
                modifier = Modifier.align(Alignment.End),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSection(
    modifier: Modifier,
    it: Int,
    weather: UiState,
    drawerState: DrawerState,
    scope: CoroutineScope
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
                .padding(top = 10.dp, start = 10.dp),
            onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White
            )
        }

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
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = weather.feelsLike ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontSize = 20.sp
            )
            Text(
                text = weather.weatherMainDescription?.uppercase() ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontSize = 35.sp
            )
            Text(
                text = weather.address ?: "",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}