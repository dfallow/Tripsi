package com.example.tripsi.screens.currentTrip

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.tripsi.R
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.data.Location as LocationData
import com.example.tripsi.utils.Location
import com.example.tripsi.utils.Screen

val viewModel = CurrentTripViewModel()

@Composable
fun CurrentTripView(location: Location, context: Context, navController: NavController, tripDbViewModel: TripDbViewModel) {

    location.startUpdatingLocation()

    val trip = tripDbViewModel.tripData.trip
    val locations = tripDbViewModel.tripData.location

    Log.d("currentTrip", trip.toString())
    Log.d("currentTrip", locations.toString())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CurrentTripMap(context = context, location = location, tripDbViewModel = tripDbViewModel)

        if (trip?.status == TripStatus.UPCOMING.status) {
            // When Trip has status UPCOMING
            Spacer(modifier = Modifier.height(64.dp))

            StartTrip(context = context, location = location, tripDbViewModel = tripDbViewModel)
        } else {
            // When Trip has status ACTIVE
            CurrentTripExtra(navController = navController, context = context)

            Spacer(modifier = Modifier.height(32.dp))

            EndTrip(context = context, location = location, tripDbViewModel = tripDbViewModel)
        }

    }

    if (viewModel.showMoment) {
        ShowMoment()
    }

}

@Composable
fun CurrentTripMap(context: Context, location: Location, tripDbViewModel: TripDbViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.7F)) {
        ShowCurrentTripMap(location, context, tripDbViewModel)

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
}

// For Save Moment and Connect a Friend Buttons
@Composable
fun CurrentTripExtra(navController: NavController, context: Context) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround) {
        Button(
            onClick = {
                navController.navigate(Screen.MomentScreen.route)
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape
        ) {
            Text("Save the Moment", textAlign = TextAlign.Center)
        }

        Button(
            onClick = {
                /*TODO*/
                Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape
        ) {
            Text("Connect a Friend", textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun StartTrip(context: Context, location: Location, tripDbViewModel: TripDbViewModel) {
    Button(
        onClick = { /*TODO
                          This function should be when the user starts a trip
                        */
            //viewModel.addStartLocation(location)
            val startLocation = LocationData(
                0,
                location.userLocation.latitude,
                location.userLocation.longitude,
                "Today",
                null,
                null,
                tripDbViewModel.tripData.trip!!.tripId,
                true
            )
            //tripDbViewModel.addLocation(startLocation)
            tripDbViewModel.updateTripStatus(TripStatus.ACTIVE.status, tripDbViewModel.tripData.trip!!.tripId)
            Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
        },
        modifier = viewModel.modifier,
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
    ) {
        Text("Start Trip", textAlign = TextAlign.Center)
    }
}

@Composable
fun EndTrip(context: Context, location: Location, tripDbViewModel: TripDbViewModel) {
    Button(
        onClick = { /*TODO
                          This function should be when the user starts a trip
                        */
            tripDbViewModel.updateTripStatus(TripStatus.UPCOMING.status, tripDbViewModel.tripData.trip!!.tripId)
            Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
        },
        modifier = viewModel.modifier,
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
    ) {
        Text("End Trip", textAlign = TextAlign.Center)
    }
}

@Composable
fun ShowMoment() {
    Popup() {
        Surface(
            color = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()

            ) {
                PopupMoment(R.drawable.location_svgrepo_com)
            }
        }
    }
}
