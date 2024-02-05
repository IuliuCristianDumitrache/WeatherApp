package com.dumitrachecristian.weatherapp.utils

import android.content.Context
import android.location.Location
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class PlacesService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val placeFields = listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    private val placesClient: PlacesClient = Places.createClient(context)

    suspend fun searchLocation(searchText: String): List<Place>? {
        if (searchText.isEmpty()) {
            return null
        }
        return suspendCancellableCoroutine { continuation ->
            val request = SearchByTextRequest.newInstance(searchText, placeFields)

            val placeResult = placesClient.searchByText(request)

            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val places: List<Place>? = task.result?.places
                    if (places != null) {
                        continuation.resume(places)
                    } else {
                        continuation.resume(null)
                    }
                } else {
                    continuation.resume(null)
                }
            }
        }
    }
}