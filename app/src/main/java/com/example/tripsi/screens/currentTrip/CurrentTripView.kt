package com.example.tripsi.screens.currentTrip

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.MutableLiveData
import com.example.tripsi.R
import com.example.tripsi.utils.Location
import com.example.tripsi.utils.Screen

val viewModel = CurrentTripViewModel()

@Composable
fun CurrentTripView(location: Location, context: Context, navController: NavController) {

    location.startUpdatingLocation()
    var currentSteps = StepCounter().toString()

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
                TripInfoOverlay(type = "Steps", measurement = currentSteps)
                TripInfoOverlay(type = "Time", measurement = "2-3hours")
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                onClick = {
                          navController.navigate(Screen.MomentScreen.route)
                },
                modifier = viewModel.modifier,
                shape = viewModel.shape
            ) {
                Text("Save the Moment", textAlign = TextAlign.Center)
            }

            Button(
                onClick = {
                            /*TODO*/
                            Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
                          },
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
                Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
                      },
            modifier = viewModel.modifier,
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            ) {
            Text("End Trip", textAlign = TextAlign.Center)
        }
    }

    if (viewModel.showMoment) {
        Popup() {
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
                    PopupMoment(R.drawable.location_svgrepo_com)
                }
            }


        }
    }

}
