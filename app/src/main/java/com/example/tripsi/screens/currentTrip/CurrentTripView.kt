package com.example.tripsi.screens.currentTrip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripsi.R
import com.example.tripsi.utils.Location
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun CurrentTripView(location: Location) {

    val viewModel = CurrentTripViewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7F)) {
            ShowCurrentTripMap(location)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                TripInfoOverlay(type = "Distance", measurement = "79km")
                TripInfoOverlay(type = "Speed", measurement = "40km/h")
                TripInfoOverlay(type = "Time", measurement = "2-3hours")
            }

        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                onClick = { /*TODO*/ },
                modifier = viewModel.modifier,
                shape = viewModel.shape
            ) {
                Text("Save the Moment", textAlign = TextAlign.Center)
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = viewModel.modifier,
                shape = viewModel.shape
            ) {
                Text("Connect a Friend", textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /*TODO*/
                      location.startUpdatingLocation()
                      },
            modifier = viewModel.modifier,
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            ) {
            Text("End Trip", textAlign = TextAlign.Center)
        }
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

// Display map in composable
@Composable
fun ShowCurrentTripMap(location: Location) {
    val currentTripMap = currentTripMap()
    var mapInitialized by remember(currentTripMap) { mutableStateOf(false)}
    val marker = Marker(currentTripMap)

    if (!mapInitialized) {
        currentTripMap.setTileSource(TileSourceFactory.MAPNIK)
        currentTripMap.setMultiTouchControls(true)
        currentTripMap.controller.setZoom(11.0)
        currentTripMap.controller.setCenter(GeoPoint(60.17, 24.95))
        mapInitialized = true
    }
    AndroidView({ currentTripMap }) {
        it.controller.animateTo(location.userLocation)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.position = location.userLocation
        marker.title = location.userLocation.toString()
        currentTripMap.overlays.add(marker)
    }
}


// Trip Measurements Overlay
@Composable
fun TripInfoOverlay(type: String, measurement: String) {

    Spacer(modifier = Modifier.height(5.dp))

    Column(
        modifier = Modifier
            .size(width = 80.dp, height = 60.dp)
            //.border(1.dp, MaterialTheme.colors.onBackground, CircleShape)
            .background(MaterialTheme.colors.onBackground, RoundedCornerShape(10.dp))
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(type, color = MaterialTheme.colors.primary)
        Text(measurement, color = MaterialTheme.colors.onSecondary)
    }



}