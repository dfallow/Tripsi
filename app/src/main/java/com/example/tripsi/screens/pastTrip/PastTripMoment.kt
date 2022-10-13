package com.example.tripsi.screens.pastTrip

import android.content.Context
import android.location.Geocoder
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.media.viewModel
import com.example.tripsi.utils.LoadingSpinner
import java.util.*

@Composable
fun PastTripMoment(
    tripDbViewModel: TripDbViewModel,
    context: Context
) {
    val pastTripData = tripDbViewModel.pastTripData
    var cityName by remember {
        mutableStateOf("")
    }

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.White)
    ) {

        pastTripData.let {

            if (it.location?.isNotEmpty() == true) {
                val geoCoder = Geocoder(context, Locale.getDefault())
                val address = geoCoder.getFromLocation(
                    it.location!![0].coordsLatitude,
                    it.location!![0].coordsLongitude,
                    1
                )
                cityName = address[0].locality
            }
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
                    DisplayPastMomentMedia(tripDbViewModel, context)
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
                    Text(it.location!![0].date, color = Color.Black)
                    Text("Location", color = Color.Black)
                    Text(cityName, color = Color.Black)
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
                    Text(it.image?.get(0)?.comment ?: "")

                    ClickableText(text = AnnotatedString("Close"), onClick = {
                        pastViewModel.hideMoment()
                    })
                }
            }
        }
    }
}

@Composable
fun DisplayPastMomentMedia(tripDbViewModel: TripDbViewModel, context: Context) {

    val momentWithMedia =
        tripDbViewModel.getMomentWithMedia(pastViewModel.currentMomentId).observeAsState().value
    //this list stores all image filenames and notes associated to them
    val filenamesAndNotes: MutableList<ArrayMap<String, String?>> = mutableListOf()

    //for each moment with media, extract filename and comment/note and save to filenamesAndNotes list
    momentWithMedia?.let { momentMedia ->
        val images = momentMedia.locationImages
        images?.forEach { image ->
            if (image.location == momentMedia.location?.locationId && image.filename != null) {
                filenamesAndNotes.add(arrayMapOf(Pair(image.filename, image.comment)))
            }
        }
    }
    //this is used to display loading spinner when set to true
    val loading = remember { mutableStateOf(true) }

    //go through all the filenames and retrieve images that match those filenames from storage
    LaunchedEffect(filenamesAndNotes) {
        loading.value = true
        viewModel.loadPhotosFromStorage(context, filenamesAndNotes)
        loading.value = false
    }

    //these are the Bitmaps that were retrieved from storage and rotated
    val imageBitmaps = viewModel.imageBitmaps.observeAsState()
    var momentNumber by remember { mutableStateOf(0) }
    LoadingSpinner(isDisplayed = loading.value)

    imageBitmaps.value?.let {
        if (it.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {

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
                                Icon(
                                    Icons.Rounded.ChevronRight,
                                    "arrow right",
                                    tint = Color(0xFFCBEF43)
                                )
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
                                Icon(
                                    Icons.Rounded.ChevronLeft,
                                    "arrow right",
                                    tint = Color(0xFFCBEF43)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}