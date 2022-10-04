package com.example.tripsi.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Screen

@Composable
fun HomeView(navController: NavController, tripDbViewModel: TripDbViewModel) {
    val homeViewModel = HomeViewModel()

    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is HomeView")
        Button(onClick = {
            navController.navigate(Screen.TravelsScreen.route)
        }) {
            Text(text = "trip history")
        }



        //check for upcoming trips
        val upcomingTrips = tripDbViewModel.getAllTripsDataByStatus(TripStatus.UPCOMING.status).observeAsState()
        var trip: Trip? = null

        if (upcomingTrips.value != null) {
            if(upcomingTrips.value!!.isNotEmpty()) {
                trip = upcomingTrips.value!![0].trip
            }
        }

        if (trip != null ) {
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
            }) {
                Text(text = "plan a trip")
            }
        }
    }
}