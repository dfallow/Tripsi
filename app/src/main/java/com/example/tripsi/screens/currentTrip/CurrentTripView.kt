package com.example.tripsi.screens.currentTrip

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripsi.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import kotlin.math.log

@Composable
fun CurrentTripView() {

    val viewModel = CurrentTripViewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7F)) {
            ShowCurrentTripMap()
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
            onClick = { /*TODO*/ },
            modifier = viewModel.modifier,
            shape = viewModel.shape
            ) {
            Text("End Trip", textAlign = TextAlign.Center)
        }
    }

}

// Initial creation of the map
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

// Display map in composable
@Composable
fun ShowCurrentTripMap() {
    val currentTripMap = CurrentTripMap()

    var mapInitialized by remember(currentTripMap) { mutableStateOf(false)}

    if (!mapInitialized) {
        currentTripMap.setTileSource(TileSourceFactory.MAPNIK)
        currentTripMap.setMultiTouchControls(true)
        currentTripMap.controller.setZoom(15.0)
        currentTripMap.controller.setCenter(GeoPoint(60.17, 24.95))
        mapInitialized = true
    }
    AndroidView({ currentTripMap })
}