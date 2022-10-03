package com.example.tripsi.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.tripsi.R
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Screen

@Composable

fun HomeView(navController: NavController, tripDbViewModel: TripDbViewModel) {
    val homeViewModel = HomeViewModel()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.globe))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)
    
    Column(
                        modifier = Modifier . fillMaxSize (),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .size(500.dp, 250.dp)
        )
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.End){

                        Button(
                            onClick = {
                                navController.navigate(Screen.TravelsScreen.route)
                            },
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
                            Text(text = "Trip history")
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start) {
                        //check for upcoming trips
                        val upcomingTrips =
                            tripDbViewModel.getAllTripsDataByStatus(TripStatus.UPCOMING.status)
                                .observeAsState()
                        var trip: Trip? = null

                        if (upcomingTrips.value != null) {
                            if (upcomingTrips.value!!.isNotEmpty()) {
                                trip = upcomingTrips.value!![0].trip
                            }
                        }

                        if (trip != null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Your trip to ${trip.destination} is coming up.")
                                Text("Ready to start?")
                                Button(onClick = {
                                    tripDbViewModel.tripId = trip.tripId
                                    navController.navigate(Screen.CurrentScreen.route)
                                }) {
                                    Text(text = "start a trip")
                                }
                            }
                        } else {
                            Button(onClick = {
                                navController.navigate(Screen.PlanScreen.route)
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
                                Text(text = "Plan a trip")
                            }
                        }
                    }
    }

}