package com.example.tripsi.screens.currentTrip

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.graphics.alpha
import com.example.tripsi.R
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.weather.WeatherCard
import com.example.tripsi.screens.weather.WeatherViewModel
import com.example.tripsi.data.Location as LocationData
import com.example.tripsi.utils.Location
import com.example.tripsi.utils.Screen
import java.util.UUID

val viewModel = CurrentTripViewModel()

@Composable
fun CurrentTripView(
    location: Location,
    context: Context,
    navController: NavController,
    tripDbViewModel: TripDbViewModel,
    weatherViewModel: WeatherViewModel
) {

    // Start updating users location when they are looking at the map
    location.startUpdatingLocation()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CurrentTripMap(
            context = context,
            location = location,
            tripDbViewModel = tripDbViewModel,
            weatherViewModel
        )

        when (viewModel.currentStatus) {
            // Upcoming
            1 -> {
                Spacer(modifier = Modifier.height(64.dp))

                StartTrip(context = context, location = location, tripDbViewModel = tripDbViewModel)
            }
            // ACTIVE
            2 -> {
                CurrentTripExtra(
                    navController = navController,
                    context = context,
                    location = location,
                    tripDbViewModel = tripDbViewModel
                )

                Spacer(modifier = Modifier.height(32.dp))

                EndTrip(context = context, location = location, tripDbViewModel = tripDbViewModel)

                // This helps update UI when user adds a moment
                if (viewModel.showText) {
                }
            }
            // PAST
            3 -> {
                GoHomeButton(navController = navController, location = location)
            }
        }


    }

    if (viewModel.showMoment) {

        val currentMoments = viewModel.currentTripMomentsNew.observeAsState().value
        var match = false


        if (currentMoments == null) {
            // This will only popup moments from the database i.e. when the map first loads
            for (moment in tripDbViewModel.currentTripMomentsNew) {
                if (viewModel.currentMomentId == moment.id) {
                    ShowMoment(tripDbViewModel,true, moment, context)
                }
            }
        } else {
            // When the user has just added a moment
            for (moment in currentMoments) {
                if (viewModel.currentMomentId == moment.id) {
                    match = true
                    ShowMoment(tripDbViewModel,false, moment, context)

                }
            }
            if (!match) {
                for (moment in tripDbViewModel.currentTripMomentsNew) {
                    if (viewModel.currentMomentId == moment.id) {
                        ShowMoment(tripDbViewModel, true, moment, context)
                    }
                }
            }
        }


    }

}

@Composable
fun CurrentTripMap(
    context: Context,
    location: Location,
    tripDbViewModel: TripDbViewModel,
    weatherViewModel: WeatherViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7F)
    ) {
        ShowCurrentTripMap(location, context, tripDbViewModel)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {

            //Weather Card
            Row(
                modifier = Modifier.fillMaxWidth()

            ) {
                Column(
                ) {
                    WeatherCard(
                        state = weatherViewModel.state,
                        backgroundColor = Color(0xFF3C493F)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (weatherViewModel.state.isLoading) {
                    CircularProgressIndicator(
                    )
                }
                weatherViewModel.state.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(
                modifier = Modifier.weight(1f),
            )
            TripInfoOverlay(type = "Distance", measurement = "79km")
            TripInfoOverlay(type = "Speed", measurement = "40km/h")
            TripInfoOverlay(type = "Time", measurement = "2-3hours")
        }

    }
}

// For Save Moment and Connect a Friend Buttons
@Composable
fun CurrentTripExtra(
    navController: NavController,
    context: Context,
    location: Location,
    tripDbViewModel: TripDbViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = {
                viewModel.currentLocation = location.userLocation
                //clear all previous moment's data from viewModel
                viewModel.clearData()
                //then navigate to MomentScreen
                navController.navigate(Screen.MomentScreen.route)
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape
        ) {
            Text("Save the Moment", textAlign = TextAlign.Center)
        }

        Button(
            onClick = {
                Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape
        ) {
            Text("Connect a Friend", textAlign = TextAlign.Center)
        }
    }
}

// Map will only show users locations until startTrip is pressed
@Composable
fun StartTrip(context: Context, location: Location, tripDbViewModel: TripDbViewModel) {
    Button(
        onClick = {
            viewModel.addStartLocationNew(location)
            val startLocation = LocationData(
                viewModel.momentId.value,
                location.userLocation.latitude,
                location.userLocation.longitude,
                "Today",
                tripDbViewModel.tripData.trip!!.tripId,
                position = MomentPosition.START,
                isStart = true,
                isEnd = false
            )
            tripDbViewModel.addLocation(startLocation)
            viewModel.startActive()
            tripDbViewModel.updateTripStatus(TripStatus.ACTIVE.status, tripDbViewModel.tripData.trip!!.tripId)
            viewModel.momentId.value = UUID.randomUUID().toString()
        },
        modifier = viewModel.modifier,
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
    ) {
        Text("Start Trip", textAlign = TextAlign.Center)
    }
}

// When the user wants to end their trip
// TODO add a popup confirmation?
@Composable
fun EndTrip(context: Context, location: Location, tripDbViewModel: TripDbViewModel) {
    Button(
        onClick = {
            viewModel.addEndLocationNew(location)
            val endLocation = LocationData(
                viewModel.momentId.value,
                location.userLocation.latitude,
                location.userLocation.longitude,
                "Today",
                tripDbViewModel.tripData.trip!!.tripId,
                position = MomentPosition.END,
                isStart = false,
                isEnd = true
            )
            tripDbViewModel.addLocation(endLocation)
            viewModel.endActive()
            tripDbViewModel.updateTripStatus(TripStatus.PAST.status, tripDbViewModel.tripData.trip!!.tripId)
            viewModel.momentId.value = UUID.randomUUID().toString()
        },
        modifier = viewModel.modifier,
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
    ) {
        Text("End Trip", textAlign = TextAlign.Center)
    }
}

// Trip Status is PAST, user can still view their trip in this screen
@Composable
fun GoHomeButton(navController: NavController, location: Location) {
    Spacer(modifier = Modifier.height(64.dp))
    Button(
        onClick = {
            viewModel.goHome(location = location)

            // navigates to home screen, doesn't allow user to navigate back to CurrentTrip
            navController.navigateUp()
        },
        modifier = viewModel.modifier,
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
    )
    {
        Text("Go home")
    }
}

// When the use clicks on a moment on the map
@Composable
fun ShowMoment(
    tripDbViewModel: TripDbViewModel,
    fromDatabase: Boolean,
    moment: CurrentTripViewModel.Moment,
    context: Context
) {
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
                if (fromDatabase) {
                    DatabaseMoment(tripDbViewModel, context)
                } else {
                    TemporaryMoment(moment)
                }
            }
        }
    }
}
