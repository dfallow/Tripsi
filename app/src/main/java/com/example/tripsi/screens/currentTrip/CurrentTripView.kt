package com.example.tripsi.screens.currentTrip

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tripsi.utils.Location

val viewModel = CurrentTripViewModel()

@Composable
fun CurrentTripView(location: Location, context: Context) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7F)) {
            ShowCurrentTripMap(location, context)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                TripInfoOverlay(type = "Distance", measurement = "79km")
                TripInfoOverlay(type = "Speed", measurement = "40km/h")
                TripInfoOverlay(type = "Time", measurement = "2-3hours")
            }

        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                onClick = { /*TODO*/ },
                modifier = viewModel.modifier,
                shape = viewModel.shape
            ) {
                Text("Save the Moment", textAlign = TextAlign.Center)
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = viewModel.modifier,
                shape = viewModel.shape
            ) {
                Text("Connect a Friend", textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /*TODO
                          This function should be when the user starts a trip
                        */
                      location.startUpdatingLocation()
                      },
            modifier = viewModel.modifier,
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            ) {
            Text("End Trip", textAlign = TextAlign.Center)
        }
    }

}

