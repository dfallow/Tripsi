package com.example.tripsi.screens.media

import android.content.Context
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.example.tripsi.R
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.LoadingSpinner
import com.example.tripsi.utils.Screen

val viewModel = MediaViewModel()

@Composable
fun MediaView(
    tripDbViewModel: TripDbViewModel,
    tripId: Int,
    navController: NavController,
    context: Context
) {
    var delete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    //get all data from database associated with a trip
    val tripData = tripDbViewModel.getTripData(tripId).observeAsState()

    val textToast = stringResource(R.string.tripDeleted)

    LaunchedEffect(tripData.value) {
        tripData.value?.let { viewModel.getStartEndCoords(it, context) }
    }

    // Store tripData in viewModel for PastTripMap
    if (tripData.value != null) {
        tripDbViewModel.pastTripData = tripData.value!!
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tripData.value.let {
            if (it != null) {
                DisplayTitle(it.trip?.tripName ?: "")
                DisplayRoute()
                DisplayStats(
                    distance = it.stats?.distance ?: 0,
                    steps = it.stats?.steps ?: 0,
                )
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {

                    DisplayTripMediaList(it.trip!!.tripId, tripDbViewModel, context)
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 70.dp)
                    ) {
                        Button(
                            onClick = {
                                navController.navigate(Screen.PastTripScreen.route)
                            },
                            enabled = !delete,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text(stringResource(R.string.showMap))
                        }
                        Button(
                            onClick = {
                                delete = true
                            },
                            modifier = Modifier
                                .height(35.dp),
                            enabled = !delete,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.error,
                                contentColor = MaterialTheme.colors.onSurface,
                            )
                        ) {
                            Text(stringResource(R.string.deleteTrip_Btn))
                        }
                    }
                    if (delete) {
                        Popup(
                            alignment = Alignment.CenterEnd,
                            properties = PopupProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false
                            ),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(MaterialTheme.colors.primaryVariant)
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Text(
                                        stringResource(R.string.areYouSure),
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = MaterialTheme.colors.secondaryVariant,
                                                contentColor = MaterialTheme.colors.primaryVariant
                                            ),
                                            onClick = {
                                                delete = false
                                                viewModel.deleteTrip(
                                                    tripId,
                                                    tripDbViewModel,
                                                    context
                                                )
                                                Toast.makeText(
                                                    context,
                                                    textToast,
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                navController.navigateUp()
                                            }
                                        ) {
                                            Text(stringResource(R.string.yesDelete))
                                        }
                                        Spacer(Modifier.size(20.dp))
                                        Button(
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(35.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = MaterialTheme.colors.error,
                                                contentColor = MaterialTheme.colors.onSurface
                                            ),
                                            onClick = { delete = false }) {
                                            Text(stringResource(R.string.no))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//displays trip name that the user entered when planning the trip
@Composable
fun DisplayTitle(tripName: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            tripName,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

//displays start and end points of the trip
@Composable
fun DisplayRoute() {
    val startLocation = viewModel.startCity.observeAsState()
    val endLocation = viewModel.endCity.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                Icons.Rounded.Home,
                "home",
                Modifier.size(20.dp),
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                startLocation.value ?: stringResource(R.string.homeNav),
                Modifier.padding(horizontal = 5.dp),
                color = MaterialTheme.colors.onPrimary
            )
        }
        Icon(
            Icons.Rounded.ChevronRight,
            "arrow",
            Modifier.size(20.dp),
            tint = MaterialTheme.colors.onPrimary
        )
        Row {
            Icon(
                Icons.Rounded.LocationOn,
                "destination",
                Modifier.size(20.dp),
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                endLocation.value ?: stringResource(R.string.destination),
                Modifier.padding(horizontal = 5.dp),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun DisplayStats(distance: Int, steps: Int) {
    Row(
        Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatsItem(label = stringResource(R.string.distance), statsValue = distance.toString(), unit = "meters")
        StatsItem(label = stringResource(R.string.steps), statsValue = steps.toString(), unit = "steps")
    }
}

//TODO: replace with component from map view?
@Composable
fun StatsItem(label: String, statsValue: String, unit: String) {
    Column(
        modifier = Modifier
            .size(97.dp, 80.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.primaryVariant)
    ) {
        Row(modifier = Modifier.padding(5.dp, 5.dp, 0.dp, 0.dp)) {
            when (label) {
                stringResource(R.string.distance) -> {
                    Icon(
                        Icons.Rounded.NearMe,
                        "distance",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                }
                stringResource(R.string.steps) -> {
                    Icon(
                        Icons.Rounded.DirectionsWalk,
                        "walking",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colors.secondaryVariant

                    )
                }
            }
            Text(
                label,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Text(
            statsValue,
            color = MaterialTheme.colors.secondaryVariant,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 7.dp)
        )
        Text(
            unit,
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(horizontal = 7.dp)
        )
    }
}

//displays all trips images and notes in a LazyRow
@Composable
fun DisplayTripMediaList(tripId: Int, tripDbViewModel: TripDbViewModel, context: Context) {
    //get all trip's location that have images
    val tripLocationsWithMedia = tripDbViewModel.getLocationWithMedia(tripId).observeAsState()

    //this list stores all image filenames and notes associated to them
    val filenamesAndNotes: MutableList<ArrayMap<String, String?>> = mutableListOf()

    //for each location that has media, extract filename and comment/note and save to filenamesAndNotes list
    tripLocationsWithMedia.value?.forEach { locationWithMedia ->
        val images = locationWithMedia.locationImages
        images?.forEach { image ->
            if (image.filename != null) {
                filenamesAndNotes.add(arrayMapOf(Pair(image.filename, image.comment)))
            }
        }
    }

    //this is used to display loading spinner when set to true
    val loading = remember { mutableStateOf(false) }

    //go through all the filenames and retrieve images that match those filenames from storage
    LaunchedEffect(filenamesAndNotes) {
        loading.value = true
        viewModel.loadPhotosFromStorage(context, filenamesAndNotes)
        loading.value = false
    }

    //these are the Bitmaps that were retrieved from storage and rotated
    val imageBitmaps = viewModel.imageBitmaps.observeAsState()

    LoadingSpinner(isDisplayed = loading.value)


    if (filenamesAndNotes.isNotEmpty()) {
        LazyRow(Modifier.padding(horizontal = 10.dp)) {
            imageBitmaps.value?.let {
                itemsIndexed(it.toList()) { _, image ->
                    rememberScrollState()
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.6f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colors.onSurface),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (image != null) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (loading.value) {
                                    LoadingSpinner(isDisplayed = loading.value)
                                } else {
                                    TripPhotoItem(image)
                                    Spacer(
                                        modifier = Modifier
                                            .size(10.dp)
                                    )
                                    image.note?.let { note -> TripNoteItem(note) }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Text(stringResource(R.string.noPhotoSaved), color = MaterialTheme.colors.onPrimary)
    }
}

@Composable
fun TripPhotoItem(image: InternalStoragePhoto) {

    var isLargePhotoVisible by remember { mutableStateOf(false) }

    if (isLargePhotoVisible) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier
                    .background(MaterialTheme.colors.onBackground)
                    .fillMaxSize()
                    .clickable {
                        isLargePhotoVisible = false
                    }) {
                Image(
                    image.bmp.asImageBitmap(), "trip photo", modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
                Icon(
                    Icons.Rounded.Close,
                    "close",
                    Modifier
                        .size(50.dp)
                        .padding(10.dp)
                        .clickable { isLargePhotoVisible = false },
                    tint = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    } else {
        Box(contentAlignment = Alignment.TopEnd) {
            Image(
                image.bmp.asImageBitmap(), "trip photo", modifier = Modifier
                    .size(230.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colors.onSurface)
                    .clickable { isLargePhotoVisible = true },
                contentScale = ContentScale.FillWidth
            )
            Icon(
                Icons.Rounded.OpenInFull,
                "expand",
                Modifier
                    .size(50.dp)
                    .padding(10.dp),
                tint = MaterialTheme.colors.secondaryVariant
            )
        }

    }
}

@Composable
fun TripNoteItem(note: String) {
    Text(
        note,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colors.primaryVariant
    )
}
