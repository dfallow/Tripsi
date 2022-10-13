package com.example.tripsi.screens.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.tripsi.R
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripData
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.weather.WeatherViewModel
import com.example.tripsi.utils.Screen


@Composable
fun HomeView(navController: NavController, tripDbViewModel: TripDbViewModel, context: Context, weatherViewModel: WeatherViewModel) {

    val homeViewModel = HomeViewModel()
    var quicktrip by remember { mutableStateOf(false) }
    var quicktripName by remember { mutableStateOf("") }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.globe))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val upcomingTrips =
        tripDbViewModel.getAllTripsDataByStatus(TripStatus.UPCOMING.status)
            .observeAsState()
    val activeTrips =
        tripDbViewModel.getAllTripsDataByStatus(TripStatus.ACTIVE.status)
            .observeAsState()
    var trip: Trip? = null
    var tripData: TripData? = null

    if (upcomingTrips.value != null) {
        if (upcomingTrips.value!!.isNotEmpty()) {
            trip = upcomingTrips.value!![0].trip
            tripData = upcomingTrips.value!![0]
        }
    }
    if (activeTrips.value != null) {
        if (activeTrips.value!!.isNotEmpty()) {
            trip = activeTrips.value!![0].trip
            tripData = activeTrips.value!![0]
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        )

    {

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .size(500.dp, 250.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    disabledBackgroundColor = MaterialTheme.colors.primary,
                    disabledContentColor = MaterialTheme.colors.onSurface
                ),
                onClick = {
                    navController.navigate(Screen.PlanScreen.route)
                },
                enabled = trip == null,
                shape = RoundedCornerShape(
                    topStartPercent = 25,
                    bottomStartPercent = 25
                ),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 15.dp,
                    end = 30.dp,
                    bottom = 15.dp
                )
            ) {
                Text(text = "Plan a trip")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                ),
                onClick = {
                    navController.navigate(Screen.TravelsScreen.route)
                },
                shape = RoundedCornerShape(
                    topEndPercent = 25,
                    bottomEndPercent = 25
                ),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 15.dp,
                    end = 30.dp,
                    bottom = 15.dp
                )
            ) {
                Text(text = "Trip History")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    disabledBackgroundColor = MaterialTheme.colors.primary,
                    disabledContentColor = MaterialTheme.colors.onSurface
                ),
                onClick = {
                    quicktrip = true
                    //homeViewModel.startQuickTrip(tripDbViewModel, context)
                    //navController.navigate(Screen.CurrentScreen.route)
                },
                enabled = trip == null,
                shape = RoundedCornerShape(
                    topStartPercent = 25,
                    bottomStartPercent = 25
                ),

                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 15.dp,
                    end = 30.dp,
                    bottom = 15.dp
                )
            )
            {
                Text(text = "Quick Trip")
            }
        }


        if (quicktrip) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        //.background(Color(0xFF2D0320))
                        .fillMaxWidth(0.9f)
                        .padding(20.dp)
                ) {
                    Column(Modifier.border(2.dp, color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(10.dp))) {
                        OutlinedTextField(
                            value = quicktripName,
                            onValueChange = { quicktripName = it},
                            maxLines = 1,
                            label = { Text("Please enter a title for your trip", color = MaterialTheme.colors.onPrimary) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.onPrimary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                                textColor = MaterialTheme.colors.onPrimary
                            ),
                            modifier = Modifier.padding(10.dp)
                        )

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)) {
                            Button(onClick = {
                                quicktrip = false
                                if (quicktripName != "") {
                                    homeViewModel.startQuickTrip(tripDbViewModel, context, quicktripName)
                                } else {
                                    homeViewModel.startQuickTrip(tripDbViewModel, context, null)
                                }
                            }) {
                                Text("Save")
                            }
                            Spacer(Modifier.size(10.dp))
                            Button(onClick = { quicktrip = false }) {
                                Text("Cancel")
                            }
                        }

                    }
                }

            }

        }

        //check for upcoming trips
        if (tripData != null) {
            UpcomingOrActiveTrip(navController = navController, tripDbViewModel = tripDbViewModel, tripData = tripData)
        }
    }
}

@Composable
fun UpcomingOrActiveTrip(navController: NavController, tripDbViewModel: TripDbViewModel, tripData: TripData) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 20.dp)
        .clip(
            shape = RoundedCornerShape(
                topEndPercent = 10,
                topStartPercent = 10
            )
        )
        .background(color = MaterialTheme.colors.primaryVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ){
        if (tripData.trip?.destination == "Unknown") {
            Text("Your trip is coming up.", color = MaterialTheme.colors.onSurface,fontSize = 20.sp)
        } else {
            Text("Your trip to ${tripData.trip?.destination} is coming up.", color = MaterialTheme.colors.onSurface,fontSize = 20.sp)
        }
        Text("Ready to start?", color = MaterialTheme.colors.onSurface, fontSize = 20.sp)
        Spacer(Modifier.size(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary),
            onClick = {

            tripDbViewModel.tripData = tripData
            // TODO
            tripDbViewModel.getCurrentTripMomentsNew(tripData.location!!)
            tripDbViewModel.getTripMoments(tripData.location!!)
            navController.navigate(Screen.CurrentScreen.route)
        },
            shape = RoundedCornerShape(
                25
            )
        ) {
            if (tripData.trip?.status == TripStatus.ACTIVE.status) {
                Text("Continue Trip")
            } else {
                Text(text = "Start Trip")
            }

        }
    }
}
