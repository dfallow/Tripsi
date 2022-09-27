package com.example.tripsi.screens.pastTrip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel

@Composable
fun PastTripItem() {

}

@Composable
fun PastTripList(model: TripDbViewModel) {
    val pastTrips = model.getAllTripsDataByStatus(TripStatus.PAST.status).observeAsState(listOf())

    LazyColumn() {
        itemsIndexed(pastTrips.value) { _, trip ->
            Column() {
                Text(trip.trip.toString())
                Text(trip.image.toString())
                Text(trip.note.toString())
                Text(trip.location.toString())
            }

        }
    }

}
