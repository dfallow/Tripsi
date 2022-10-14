package com.example.tripsi.screens.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.tripsi.R
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripData
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.weather.WeatherViewModel
import com.example.tripsi.utils.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue


@Composable
fun HomeView(
    navController: NavController,
    tripDbViewModel: TripDbViewModel,
    context: Context,
    weatherViewModel: WeatherViewModel
) {

    val focusManager = LocalFocusManager.current
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
                    Column(
                        Modifier.border(
                            2.dp,
                            color = MaterialTheme.colors.primaryVariant,
                            shape = RoundedCornerShape(10.dp)
                        )
                    ) {
                        OutlinedTextField(
                            value = quicktripName,
                            onValueChange = { quicktripName = it },
                            maxLines = 1,
                            label = {
                                Text(
                                    "Please enter a title for your trip",
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions {
                                focusManager.clearFocus(true)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.onPrimary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                                textColor = MaterialTheme.colors.onPrimary
                            ),
                            modifier = Modifier.padding(10.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.End, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Button(onClick = {
                                    quicktrip = false
                                    if (quicktripName != "") {
                                        homeViewModel.startQuickTrip(
                                            tripDbViewModel,
                                            context,
                                            quicktripName
                                        )
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
        }

        //check for upcoming trips
        if (tripData != null) {
            UpcomingOrActiveTrip(
                navController = navController,
                tripDbViewModel = tripDbViewModel,
                tripData = tripData
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UpcomingOrActiveTrip(
    navController: NavController,
    tripDbViewModel: TripDbViewModel,
    tripData: TripData
) {
    var changeIcon by remember { mutableStateOf(false) }
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
        if (tripData.trip?.destination == "Unknown") {
            Text(
                "Your trip is coming up.",
                color = MaterialTheme.colors.onSurface,
                fontSize = 20.sp
            )
        } else {
            Text(
                "Your trip to ${tripData.trip?.destination} is coming up.",
                color = MaterialTheme.colors.onSurface,
                fontSize = 20.sp
            )
        }
        Text("Ready to start?", color = MaterialTheme.colors.onSurface, fontSize = 20.sp)

        Spacer(Modifier.size(20.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            ),
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
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.width(40.dp))
                Image(
                    when (tripData.trip!!.travelMethod) {
                        1 -> painterResource(R.drawable.car_svgrepo_com)
                        2 -> painterResource(R.drawable.bike_svgrepo_com)
                        3 -> painterResource(R.drawable.hiker_walk_svgrepo_com)
                        4 -> painterResource(R.drawable.plane_svgrepo_com)
                        else -> painterResource(R.drawable.bus_svgrepo_com)
                    },
                    contentDescription = "user map icons",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colors.onSurface, CircleShape)
                        .clickable { changeIcon = true }
                )
            }



            Spacer(modifier = Modifier.height(0.dp))

        }

        if (changeIcon) {
            Popup {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight(0.45f)
                            .fillMaxWidth()
                            .background(Color.Black)
                    ) {
                        Text(
                            "Swipe to see your options\n Tap to confirm",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        HorizontalPager(
                            count = 5,
                            state = rememberPagerState(tripData.trip!!.travelMethod - 1)
                        ) { icon ->
                            Box {
                                Card(
                                    Modifier
                                        .border(
                                            3.dp,
                                            Color.White,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .fillMaxSize(0.7f)
                                        .graphicsLayer {

                                            val pageOffset =
                                                calculateCurrentOffsetForPage(icon).absoluteValue

                                            lerp(
                                                start = 0.85f,
                                                stop = 1f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            ).also { scale ->
                                                scaleX = scale
                                                scaleY = scale
                                            }

                                            alpha = lerp(
                                                start = 0.5f,
                                                stop = 1f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                        }
                                ) {
                                    Image(
                                        when (icon) {
                                            0 -> painterResource(R.drawable.car_svgrepo_com)
                                            1 -> painterResource(R.drawable.bike_svgrepo_com)
                                            2 -> painterResource(R.drawable.hiker_walk_svgrepo_com)
                                            3 -> painterResource(R.drawable.plane_svgrepo_com)
                                            else -> painterResource(R.drawable.bus_svgrepo_com)
                                        },
                                        contentDescription = "user map icons",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clickable {
                                                changeIcon = false
                                                tripDbViewModel.updateTripTravelMethod(
                                                    icon + 1,
                                                    tripData.trip!!.tripId
                                                )
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
