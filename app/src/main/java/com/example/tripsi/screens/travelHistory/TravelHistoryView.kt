package com.example.tripsi.screens.travelHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tripsi.R
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Screen

//retrieve all trips that already happened (status = PAST) and display them in a list
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TravelHistoryView(tripDbViewModel: TripDbViewModel, navController: NavController) {

    val pastTrips =
        tripDbViewModel.getAllTripsDataByStatus(TripStatus.PAST.status).observeAsState(listOf())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.tripHistory),
            Modifier.padding(vertical = 15.dp),
            color = MaterialTheme.colors.onPrimary,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        if (pastTrips.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                itemsIndexed(pastTrips.value) { _, item ->
                    item.trip?.let {
                        TravelHistoryItem(
                            trip = it,
                            imageCount = item.image?.size ?: 0,
                            tripDbViewModel = tripDbViewModel,
                            navController = navController
                        )
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    stringResource(R.string.nothingToShow),
                    color = MaterialTheme.colors.onPrimary, textAlign = TextAlign.Center
                )

            }

        }

    }
}

@Composable
fun TravelHistoryItem(
    trip: Trip,
    imageCount: Int,
    tripDbViewModel: TripDbViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            //.height(90.dp)
            .background(MaterialTheme.colors.primaryVariant)
            .fillMaxHeight()
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clickable {
                tripDbViewModel.tripId = trip.tripId
                navController.navigate(Screen.MediaScreen.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(trip.tripName, fontSize = 20.sp, color = MaterialTheme.colors.secondaryVariant)
                Spacer(Modifier.size(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.3f)
                ) {
                    Row {
                        Icon(
                            Icons.Rounded.Image,
                            "image icon",
                            tint = MaterialTheme.colors.onSurface
                        )
                        Spacer(Modifier.size(5.dp))
                        Text(
                            imageCount.toString(),
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
            IconButton(onClick = {
                tripDbViewModel.tripId = trip.tripId
                navController.navigate(route = Screen.MediaScreen.route)
            }) {
                Icon(
                    Icons.Rounded.ChevronRight,
                    "arrow right",
                    tint = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }

}
