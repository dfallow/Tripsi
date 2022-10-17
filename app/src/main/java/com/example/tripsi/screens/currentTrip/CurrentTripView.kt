package com.example.tripsi.screens.currentTrip


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.tripsi.R
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.weather.WeatherCard
import com.example.tripsi.screens.weather.WeatherViewModel
import com.example.tripsi.utils.Location
import com.example.tripsi.utils.Screen
import com.example.tripsi.utils.StopWatch
import java.util.*
import com.example.tripsi.data.Location as LocationData

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
    val stopWatch = remember { StopWatch() }
    stopWatch.start()
    val textToast = stringResource(R.string.nothing_toast)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        CurrentTripMap(
            context = context,
            location = location,
            tripDbViewModel = tripDbViewModel,
            weatherViewModel,
            stopWatch
        )

        when (viewModel.currentStatus) {
            // Upcoming
            1 -> {
                Spacer(modifier = Modifier.height(64.dp))

                StartTrip(
                    context = context,
                    location = location,
                    tripDbViewModel = tripDbViewModel
                )
            }
            // ACTIVE
            2 -> {
                CurrentTripExtra(
                    navController = navController,
                    context = context,
                    location = location,
                    tripDbViewModel = tripDbViewModel
                )

                Spacer(modifier = Modifier.height(24.dp))

                EndTrip(
                    context = context,
                    location = location,
                    tripDbViewModel = tripDbViewModel
                )

                // This helps update UI when user adds a moment
                if (viewModel.showText) {
                }
            }
            // PAST
            3 -> {
                GoHomeButton(navController = navController, location = location)
            }
        }




        if (viewModel.showMoment) {

            val currentMoments = viewModel.currentTripMomentsNew.observeAsState().value
            var match = false


            if (currentMoments == null) {
                // This will only popup moments from the database i.e. when the map first loads
                for (moment in tripDbViewModel.currentTripMomentsNew) {
                    if (viewModel.currentMomentId == moment.id) {
                        ShowMoment(tripDbViewModel, true, moment, context)
                    }
                }
            } else {
                // When the user has just added a moment
                for (moment in currentMoments) {
                    if (viewModel.currentMomentId == moment.id) {
                        match = true
                        ShowMoment(tripDbViewModel, false, moment, context)

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

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    //TODO: reset steps
                    stopWatch.resetWatch()
                    viewModel.resetSteps()
                    Toast.makeText(context, textToast, Toast.LENGTH_LONG).show()
                },
                modifier = viewModel.modifier,
                shape = viewModel.shape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
            ) {
                Text(stringResource(R.string.End_Btn), textAlign = TextAlign.Center)
            }
        }
    }

}

@Composable
fun CurrentTripMap(
    context: Context,
    location: Location,
    tripDbViewModel: TripDbViewModel,
    weatherViewModel: WeatherViewModel,
    stopWatch: StopWatch
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
                        state = weatherViewModel.state
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
            StepCounterSensor()

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
            Text(stringResource(R.string.saveMoment_Btn), textAlign = TextAlign.Center)
        }
    }
}

// Map will only show users locations until startTrip is pressed
@Composable
fun StartTrip(context: Context, location: Location, tripDbViewModel: TripDbViewModel) {
    Button(
        onClick = {
            viewModel.addStartLocationNew(location)
            tripDbViewModel.addLocation(
                com.example.tripsi.data.Location(
                    viewModel.momentId.value,
                    location.userLocation.latitude,
                    location.userLocation.longitude,
                    "Today",
                    tripDbViewModel.tripData.trip!!.tripId,
                    position = MomentPosition.START,
                    isStart = true,
                    isEnd = false
                )
            )
            viewModel.startActive()
            tripDbViewModel.updateTripStatus(
                TripStatus.ACTIVE.status,
                tripDbViewModel.tripData.trip!!.tripId
            )
            viewModel.momentId.value = UUID.randomUUID().toString()
        },
        modifier = viewModel.modifier,
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
    ) {
        Text(stringResource(R.string.trip_start_Btn), textAlign = TextAlign.Center)
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
            tripDbViewModel.updateTripStatus(
                TripStatus.PAST.status,
                tripDbViewModel.tripData.trip!!.tripId
            )
            viewModel.momentId.value = UUID.randomUUID().toString()
            viewModel.saveStatisticsToDb(tripDbViewModel)
        },
        modifier = Modifier.height(35.dp),
        shape = viewModel.shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.error,
            contentColor = MaterialTheme.colors.onSurface
        ),
    ) {
        Text(stringResource(R.string.End_Btn), textAlign = TextAlign.Center)
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
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
    )
    {
        Text(stringResource(R.string.go_home))
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
            color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.6f),
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
