package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.tripsi.R
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Location
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

// Display map in composable
@Composable
fun ShowCurrentTripMap(location: Location, context: Context, tripDbViewModel: TripDbViewModel) {
    val currentTripMap = currentTripMap()
    var mapInitialized by remember(currentTripMap) { mutableStateOf(false) }
    val userLocation = Marker(currentTripMap)

    //var momentLocations = arrayOf(Marker(currentTripMap))
    val momentsFromDatabase = tripDbViewModel.currentTripMoments
    val currentTripMoments = viewModel.currentTripMoments.observeAsState().value

    val polyline: Polyline
    var momentLocations: Array<Marker>

    /*
    * Initially shows the locations from the database, but will use the temporary data
    * from the viewModel, as to constantly update the UI when new locations are added
    */
    if ((currentTripMoments == null)) {
        polyline = Polyline()
        polyline.setPoints(momentsFromDatabase)
        // Creates moment markers from locations gotten from database
        momentLocations = arrayOf(Marker(currentTripMap))
        for (moment in momentsFromDatabase) {
            val moMarker = Marker(currentTripMap)
            moMarker.position = moment
            moMarker.icon = ContextCompat.getDrawable(context, R.drawable.location_svgrepo_com)

            moMarker.setOnMarkerClickListener { marker, mapView ->
                Log.d("marker","${marker.position}")
                viewModel.displayMoment()
                true
            }
            momentLocations += moMarker
        }
    } else  {
        // used for updating ui
        polyline = Polyline()
        Log.d("CurrentTripMoments", currentTripMoments.toString())
        val polylinePoints = viewModel.createPolylinePoints()
        polyline.setPoints(polylinePoints)
        momentLocations = arrayOf(Marker(currentTripMap))
        for (moment in currentTripMoments) {
            val moMarker = Marker(currentTripMap)
            moMarker.id = moment.location.toString()
            moMarker.position = moment.location
            moMarker.icon = ContextCompat.getDrawable(context, R.drawable.location_svgrepo_com)

            moMarker.setOnMarkerClickListener { marker, mapView ->
                Log.d("marker","${marker.position}")
                viewModel.momentId.value = moMarker.id
                viewModel.displayMoment()
                Log.d("marker", "test")
                true
            }
            momentLocations += moMarker
        }
    }


    if (!mapInitialized) {
        currentTripMap.setTileSource(TileSourceFactory.MAPNIK)
        currentTripMap.setMultiTouchControls(true)
        currentTripMap.controller.setZoom(11.0)
        currentTripMap.controller.setCenter(GeoPoint(60.17, 24.95))
        mapInitialized = true
    }
    AndroidView({ currentTripMap }) {
        it.controller.animateTo(location.userLocation)
        userLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userLocation.position = location.userLocation
        userLocation.title = location.userLocation.toString()
        userLocation.icon = ContextCompat.getDrawable(context, R.drawable.hiker_walk_svgrepo_com)

        // add lines that connect moments
        currentTripMap.overlays.add(polyline)

        // adding moments to map
        for (moment in momentLocations) {
            currentTripMap.overlays.add(moment)
        }

        // add user location marker
        currentTripMap.overlays.add(userLocation)

        // TODO not sure if this is needed
        //currentTripMap.invalidate()
    }
}

// Initial creation of the map
@Composable
fun currentTripMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    return mapView
}

// Trip Measurements Overlay
// TODO add small icons, change text size, replace dummy info with liveData
@Composable
fun TripInfoOverlay(type: String, measurement: String) {

    Spacer(modifier = Modifier.height(5.dp))

    Column(
        modifier = Modifier
            .size(width = 80.dp, height = 60.dp)
            .background(MaterialTheme.colors.onBackground, RoundedCornerShape(10.dp))
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(type, color = MaterialTheme.colors.primary)
        Text(measurement, color = MaterialTheme.colors.onSecondary)
    }
}