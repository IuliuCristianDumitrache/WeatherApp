package com.dumitrachecristian.weatherapp.ui.mapscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dumitrachecristian.weatherapp.R
import com.dumitrachecristian.weatherapp.model.uimodel.UiState
import com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel.MainViewModel
import com.dumitrachecristian.weatherapp.ui.theme.TestAppComposeTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(navController: NavHostController, viewModel: MainViewModel) {
    TestAppComposeTheme {
        Box() {
            MapsComposable(viewModel)
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp, start = 10.dp),
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun MapsComposable(viewModel: MainViewModel) {
    val favoriteItems = viewModel.favoriteLocations.collectAsStateWithLifecycle()
    val markers = composeAllMarkers(favoriteItems)
    val cameraPositionState = rememberCameraPositionState {
        position = if (markers.isNotEmpty()) {
            CameraPosition.fromLatLngZoom(markers[0].latLng, 10f)
        } else {
            CameraPosition.fromLatLngZoom(LatLng(40.0,40.0), 3f)
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        for(marker in markers) {
            Marker(
                state = MarkerState(position = marker.latLng),
                title = marker.name,
                snippet = marker.temperature
            )
        }
    }
}

fun composeAllMarkers(favoriteItems: State<List<UiState>>): MutableList<MarkerUi> {
    val markerList = mutableListOf<MarkerUi>()
    for (item in favoriteItems.value) {
        markerList.add(MarkerUi(LatLng(item.latitude!!, item.longitude!!), item.address!!, item.temperature!!))
    }
    return markerList
}

data class MarkerUi(
    val latLng: LatLng,
    val name: String,
    val temperature: String
)
