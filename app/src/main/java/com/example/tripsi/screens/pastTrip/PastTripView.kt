package com.example.tripsi.screens.pastTrip

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripsi.R
import com.example.tripsi.functionality.TripDbViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

val pastViewModel = PastTripViewModel()

@Composable
fun PastTripView(context: Context, tripDbViewModel: TripDbViewModel) {
    val pastTripMap = pastTripMap()
    var mapInitialized by remember(pastTripMap) { mutableStateOf(false) }

    val distanceBetween = FloatArray(1)

    val pastTripLocations = tripDbViewModel.pastTripData.location
    if (pastTripLocations != null && pastTripLocations.isNotEmpty()) {
        android.location.Location.distanceBetween(
            pastTripLocations.first().coordsLatitude,
            pastTripLocations.first().coordsLongitude,
            pastTripLocations.last().coordsLatitude,
            pastTripLocations.last().coordsLongitude,
            distanceBetween
        )
    }

    val polyline: Polyline
    val pastLocations: Array<Marker>

    if (!mapInitialized) {
        pastTripMap.setTileSource(TileSourceFactory.MAPNIK)
        pastTripMap.setMultiTouchControls(true)

        when (distanceBetween[0].toInt()) {
            in 0..500 -> {pastTripMap.controller.setZoom(15.0)}
            in 501..5000 -> {pastTripMap.controller.setZoom(11.0)}
            in 5001..50000 -> {pastTripMap.controller.setZoom(8.0)}
            in 500001..5000000 -> {pastTripMap.controller.setZoom(5.0)}
            else -> {pastTripMap.controller.setZoom(3.0)}
        }
        pastTripMap.controller.setCenter(
            GeoPoint(pastTripLocations!![0].coordsLatitude, pastTripLocations!![0].coordsLongitude)
        )
        mapInitialized = true
    }
    AndroidView({ pastTripMap }) {

    }
}

@Composable
fun pastTripMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    return mapView
}