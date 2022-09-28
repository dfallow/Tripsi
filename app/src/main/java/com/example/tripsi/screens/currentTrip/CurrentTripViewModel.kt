package com.example.tripsi.screens.currentTrip

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class CurrentTripViewModel: ViewModel() {

    // Currently storing generic properties for buttons
    // TODO create own file for these properties
    val modifier = Modifier
        .width(130.dp)
    val shape = RoundedCornerShape(10.dp)

    // Temp array to show on map
    val moments = arrayOf(
        GeoPoint(60.209, 24.645),
        GeoPoint(60.259, 24.683),
        GeoPoint(60.301, 24.958)
    )

}