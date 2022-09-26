package com.example.tripsi.screens.currentTrip

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripsi.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun CurrentTripView() {
    ShowCurrentTripMap()
}

@Composable
fun CurrentTripMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    return mapView
}

@Composable
fun ShowCurrentTripMap() {
    val currentTripMap = CurrentTripMap()

    var mapInitialized by remember(currentTripMap) { mutableStateOf(false)}

    if (!mapInitialized) {
        currentTripMap.setTileSource(TileSourceFactory.MAPNIK)
        currentTripMap.controller.setZoom(9.0)
        currentTripMap.controller.setCenter(GeoPoint(60.17, 24.95))
    }
    AndroidView({ currentTripMap })
}