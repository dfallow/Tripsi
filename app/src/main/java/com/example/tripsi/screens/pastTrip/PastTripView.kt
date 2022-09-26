package com.example.tripsi.screens.pastTrip

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun PastTripView(navigator: DestinationsNavigator) {
    Text(text = "This is PastTripView")
}