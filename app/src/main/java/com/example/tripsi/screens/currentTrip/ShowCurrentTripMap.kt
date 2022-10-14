package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    val momentsFromDatabase = tripDbViewModel.currentTripMoments

    // TODO
    val momentsFromDatabaseNew = tripDbViewModel.currentTripMomentsNew

    // TODO
    val currentTripMomentsNew = viewModel.currentTripMomentsNew.observeAsState().value

    val polyline: Polyline
    var momentLocations: Array<Marker> = arrayOf(Marker(currentTripMap))

    fun createMomentMarkers(fromDatabase: Boolean, tripMoments: ArrayList<CurrentTripViewModel.Moment>) {
        for ((index, moment) in tripMoments.withIndex()) {
            viewModel.mapMoments.add(index)
            val moMarker = Marker(currentTripMap)
            moMarker.position = moment.location
            moMarker.id = moment.id
            if (moment.position == MomentPosition.START.position || moment.position == MomentPosition.END.position) {
                moMarker.icon = ContextCompat.getDrawable(context, R.drawable.location_svgrepo_com)
            } else {
                moMarker.icon = ContextCompat.getDrawable(context, R.drawable.photo_svgrepo_com)
            }

            if (moment.position == MomentPosition.MIDDLE.position) {
                moMarker.setOnMarkerClickListener { _, _ ->
                    viewModel.currentIndex.value = index
                    viewModel.currentMomentId = moMarker.id
                    viewModel.displayMoment()
                    true
                }
            } else {
                moMarker.setOnMarkerClickListener { _, _ ->
                    Toast.makeText(context, "This is your Start/End location", Toast.LENGTH_SHORT).show()
                    true
                }
            }

            momentLocations += moMarker
        }
    }

    /*
    * Initially shows the locations from the database, but will use the temporary data
    * from the viewModel, as to constantly update the UI when new locations are added
    */
    if ((currentTripMomentsNew == null)) {
        viewModel.fromDatabase.value = true
        viewModel.currentStatus = tripDbViewModel.tripData.trip!!.status
        polyline = Polyline()
        polyline.setPoints(momentsFromDatabase)

        createMomentMarkers(viewModel.fromDatabase.value, momentsFromDatabaseNew)
        Log.d("userLocationDatabase", "$momentsFromDatabaseNew")
        Log.d("userLocationTemporary","$currentTripMomentsNew")

    } else  {
        Log.d("userLocationDatabase", "$momentsFromDatabaseNew")
        Log.d("userLocationTemporary","$currentTripMomentsNew")
        // used for updating ui
        viewModel.fromDatabase.value = false
        polyline = Polyline()
        val polylinePoints: ArrayList<GeoPoint> = ArrayList()
        // Used to connect the new points added by the user to the existing database entries
        if (momentsFromDatabase.isNotEmpty()) { polylinePoints.add(momentsFromDatabase.last()) }
        for (moment in currentTripMomentsNew) { polylinePoints.add(moment.location) }
        polyline.setPoints(polylinePoints)

        createMomentMarkers(viewModel.fromDatabase.value, momentsFromDatabaseNew)
        createMomentMarkers(viewModel.fromDatabase.value, currentTripMomentsNew)
    }


    if (!mapInitialized) {
        currentTripMap.setTileSource(TileSourceFactory.MAPNIK)
        currentTripMap.setMultiTouchControls(true)
        currentTripMap.controller.setZoom(11.0)
        currentTripMap.controller.setCenter(GeoPoint(60.17, 24.95))
        mapInitialized = true
    }
    AndroidView({ currentTripMap }) {
        it.overlays.clear()
        it.controller.animateTo(location.userLocation)
        userLocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userLocation.position = location.userLocation

        // Changes the users icon based on their choice, Quick trip is car
        when (tripDbViewModel.tripData.trip!!.travelMethod) {
            1 -> {
                userLocation.icon = ContextCompat.getDrawable(context, R.drawable.car_svgrepo_com)
            }
            2 -> {
                userLocation.icon = ContextCompat.getDrawable(context, R.drawable.bike_svgrepo_com)
            }
            3 -> {
                userLocation.icon = ContextCompat.getDrawable(context, R.drawable.hiker_walk_svgrepo_com)
            }
            4 -> {
                userLocation.icon = ContextCompat.getDrawable(context, R.drawable.plane_svgrepo_com)
            }
            5 -> {
                userLocation.icon = ContextCompat.getDrawable(context, R.drawable.bus_svgrepo_com)
            }
            else -> {}
        }



        // add lines that connect moments
        currentTripMap.overlays.add(polyline)

        // add user location marker
        currentTripMap.overlays.add(userLocation)

        // adding moments to map
        for (moment in momentLocations) {
            currentTripMap.overlays.add(moment)
        }

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
            .background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(10.dp))
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(type, color = MaterialTheme.colors.onSurface)
        Text(measurement, color = MaterialTheme.colors.onSurface)
    }
}