package com.example.tripsi.screens.home

import android.content.Context
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.airbnb.lottie.compose.*
import com.example.tripsi.R
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripData
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.DatabaseMoment
import com.example.tripsi.screens.currentTrip.TemporaryMoment
import com.example.tripsi.screens.weather.WeatherCard
import com.example.tripsi.screens.weather.WeatherViewModel
import com.example.tripsi.utils.LockScreenOrientation
import com.example.tripsi.utils.Screen
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun HomeView(navController: NavController, tripDbViewModel: TripDbViewModel, context: Context, weatherViewModel: WeatherViewModel) {

    val homeViewModel = HomeViewModel()

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
                onClick = {
                    homeViewModel.startQuickTrip(tripDbViewModel, context)
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
            ) {
                Text(text = "Quick Trip")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topEndPercent = 10,
                        topStartPercent = 10
                    )
                )
                .background(color = Color(0xFF3C493F)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,

            ) {
            Text(
                "Your trip to ${tripData.trip?.destination} is coming up.\n Ready to start?",
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                fontSize = 20.sp
            )
            //Text("Ready to start?", color = Color.White, fontSize = 20.sp)
            Button(
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

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 15.dp)
                    //.background(Color.Black)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Your Current Icon\n click to change",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    Image(
                        when (tripData.trip!!.travelMethod) {
                            1 -> painterResource(R.drawable.car_svgrepo_com)
                            2 -> painterResource(R.drawable.bike_svgrepo_com)
                            3 -> painterResource(R.drawable.hiker_walk_svgrepo_com)
                            4 -> painterResource(R.drawable.plane_svgrepo_com)
                            else -> painterResource(R.drawable.bus_svgrepo_com) },
                        contentDescription = "user map icons",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colors.primary, CircleShape)
                    )
                }

            }
            
            Spacer(modifier = Modifier.height(0.dp))

        }
}
