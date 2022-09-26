package com.example.tripsi.screens.travelHistory

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TravelHistoryView(navigator: DestinationsNavigator) {
    Column() {
        Text(text = "This is TravelHistoryView")
    }

}