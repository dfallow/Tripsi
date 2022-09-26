package com.example.tripsi.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.tripsi.screens.destinations.PlanTripViewDestination
import com.example.tripsi.screens.destinations.TravelHistoryViewDestination
import com.example.tripsi.screens.travelHistory.TravelHistoryView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.lang.reflect.Modifier

@Destination(start = true)
@Composable
fun HomeView(navigator: DestinationsNavigator) {
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is HomeView")
        Button(onClick = {
            navigator.navigate(
                TravelHistoryViewDestination()
            )
        }) {
            Text(text = "trip history")
        }
        Button(onClick = {
            navigator.navigate(
                PlanTripViewDestination()
            )
        }) {
            Text(text = "plan a trip")
        }
    }
}