package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.util.Log
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.media.DisplayTripMediaList
import com.example.tripsi.utils.LoadingSpinner

@Composable
fun DatabaseMoment(
    tripDbViewModel: TripDbViewModel,
    moment: CurrentTripViewModel.Moment,
    context: Context
) {

    val currentTripData = tripDbViewModel.getTripData(tripDbViewModel.tripData.trip!!.tripId).observeAsState().value

    Log.d("currentMoment", "${tripDbViewModel.tripData}")



    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.White)
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                // Shows the image, currently set up with one mock image
                Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight(0.55f),
                shape = RoundedCornerShape(10)
            ) {

                currentTripData?.let {
                    var momentNumber by remember { mutableStateOf(1) }

                    //DisplayTripMediaList(it.trip!!.tripId, tripDbViewModel, context)
                    DisplayMomentMedia(it.trip!!.tripId, tripDbViewModel, context)
                }

                var momentNumber by remember { mutableStateOf(1) }
                /*if (moment.photos != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            moment.photos[momentNumber]!!.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Buttons used to switch between images
                        if (momentNumber >= 1 && momentNumber < moment.photos.size - 1) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                TextButton(
                                    //modifier = Modifier,
                                    onClick = {
                                        momentNumber += 1
                                    }
                                ) {
                                    Icon(Icons.Rounded.ChevronRight, "arrow right", tint = Color(0xFFCBEF43))
                                }
                            }

                        }
                        if (momentNumber <= moment.photos.size && momentNumber > 1) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                TextButton(
                                    onClick = {
                                        momentNumber -= 1
                                    }
                                ) {
                                    Icon(Icons.Rounded.ChevronLeft, "arrow right", tint = Color(0xFFCBEF43))
                                }
                            }

                        }
                    }
                }*/
            }
            Column(
                // Contains the moment information such as date, location, time
                Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Date", color = Color.Black)
                Text(moment.info.date, color = Color.Black)
                Text("Time", color = Color.Black)
                Text(moment.info.time, color = Color.Black)
                Text("Location", color = Color.Black)
                Text(moment.info.location, color = Color.Black)

            }
        }
        Column(
            // This contains the moment comment and hide moment clickable text
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight(0.45f)
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 10.dp)
            ) {
                Text(moment.description ?: "")

                ClickableText(text = AnnotatedString("Close"), onClick = {
                    viewModel.hideMoment()
                })
            }
        }
    }
}

@Composable
fun DisplayMomentMedia(tripId: Int, tripDbViewModel: TripDbViewModel, context: Context) {
    //get all trip's location that have images
    val tripLocationsWithMedia = tripDbViewModel.getLocationWithMedia(tripId).observeAsState()

    // TODO THIS IS THE LOCATION IT HAS THE INFO AND PHOTOS USE THIS
    val momentWithMedia = tripDbViewModel.getMomentWithMedia(viewModel.currentMomentId).observeAsState()
    Log.d("databaseMedia", "${momentWithMedia.value?.locationImages}")

    //this list stores all image filenames and notes associated to them
    val filenamesAndNotes: MutableList<ArrayMap<String, String?>> = mutableListOf()


    /*tripLocationsWithMedia.value?.forEach { locationWithMedia ->
        val images = locationWithMedia.locationImages
        images?.forEach { image ->
            if (image.filename != null) {
                filenamesAndNotes.add(arrayMapOf(Pair(image.filename, image.comment)))
            }
        }
    }*/
    //for each moment with media, extract filename and comment/note and save to filenamesAndNotes list
    momentWithMedia.value?.let { momentMedia ->
        val images = momentMedia.locationImages
        images?.forEach { image ->
            if (image.location == momentMedia.location?.locationId && image.filename != null) {
                filenamesAndNotes.add(arrayMapOf(Pair(image.filename, image.comment)))
            }
        }
    }

    //this is used to display loading spinner when set to true
    val loading = remember { mutableStateOf(false) }

    //go through all the filenames and retrieve images that match those filenames from storage
    LaunchedEffect(filenamesAndNotes) {
        loading.value = true
        com.example.tripsi.screens.media.viewModel.loadPhotosFromStorage(context, filenamesAndNotes)
        loading.value = false
    }

    //these are the Bitmaps that were retrieved from storage and rotated
    val imageBitmaps = com.example.tripsi.screens.media.viewModel.imageBitmaps.observeAsState()
    var momentNumber by remember { mutableStateOf(0) }
    LoadingSpinner(isDisplayed = loading.value)

    imageBitmaps.value?.let {
        if (it.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {

                Log.d("it.size", "${it.size}")
                if (loading.value) {
                    LoadingSpinner(isDisplayed = loading.value)
                } else {
                    Image(
                        it[momentNumber]!!.bmp.asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Buttons used to switch between images
                    if (momentNumber >= 0 && momentNumber < it.size - 1) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            TextButton(
                                //modifier = Modifier,
                                onClick = {
                                    momentNumber += 1
                                }
                            ) {
                                Icon(Icons.Rounded.ChevronRight, "arrow right", tint = Color(0xFFCBEF43))
                            }
                        }

                    }
                    if (momentNumber <= it.size && momentNumber > 0) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            TextButton(
                                onClick = {
                                    momentNumber -= 1
                                }
                            ) {
                                Icon(Icons.Rounded.ChevronLeft, "arrow right", tint = Color(0xFFCBEF43))
                            }
                        }

                    }
                }

            }
        }

    }

}