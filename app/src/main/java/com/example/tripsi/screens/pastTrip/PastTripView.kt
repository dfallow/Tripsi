package com.example.tripsi.screens.pastTrip

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import com.example.tripsi.functionality.TripDbViewModel

val pastViewModel = PastTripViewModel()

@Composable
fun PastTripView(context: Context, tripDbViewModel: TripDbViewModel) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            PastTripMap(context = context, tripDbViewModel = tripDbViewModel)

            if (pastViewModel.showMoment) {
                Popup {
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
                            for (moment in tripDbViewModel.currentTripMomentsNew) {
                                if (pastViewModel.currentMomentId == moment.id) {
                                    ShowPastMoment(context = context, tripDbViewModel = tripDbViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun ShowPastMoment(context: Context, tripDbViewModel: TripDbViewModel) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
               PastTripMoment(tripDbViewModel, context)
            }
}