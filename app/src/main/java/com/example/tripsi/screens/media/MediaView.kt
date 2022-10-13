package com.example.tripsi.screens.media


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.data.Location
import com.example.tripsi.data.LocationWithImagesAndNotes
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.CurrentTripViewModel
import com.example.tripsi.screens.currentTrip.MomentPosition
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
    //get all data from database associated with a trip
    val tripData = tripDbViewModel.getTripData(tripId).observeAsState()
    LaunchedEffect(tripData.value) {
        tripData.value?.let { viewModel.getStartEndCoords(it, context) }
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp)
                    ) {
                        Button(
                            onClick = {
                                /*TODO*/
                                Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFCBEF43),
                                contentColor = Color(0xFF2D0320)
                            )
                        ) {
                            Text("show on map")
                        }
                        Button(
                            onClick = {
                                //viewModel.deleteTrip(tripId, tripDbViewModel)
                                //navController.navigate(Screen.TravelsScreen.route)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF2D0320),
                                contentColor = Color(0xFFFFFFFF)
                            )
                        ) {
                            Text("Delete trip")
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
            color = Color(0xFF3C493F)
        )
    }
}

//displays start and end points of the trip
//currently destination = destination that the user entered when planning the trip
@Composable
fun DisplayRoute() {
    val startLocation = viewModel.startCity.observeAsState()
    val endLocation = viewModel.endCity.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                Icons.Rounded.Home,
                "home location",
                Modifier.size(20.dp),
                tint = Color(0xFF3C493F)
            )
            Text(startLocation.value ?: "Home", Modifier.padding(horizontal = 5.dp), color = Color(0xFF3C493F))
        }
        Icon(
            Icons.Rounded.ChevronRight,
            "arrow",
            Modifier.size(20.dp),
            tint = Color(0xFF3C493F)
        )
        //Text("to", color =  Color(0xFFCBEF43))
        Row {
            Icon(
                Icons.Rounded.LocationOn,
                "destination",
                Modifier.size(20.dp),
                tint = Color(0xFF3C493F)
            )
            Text(endLocation.value ?: "Destination", Modifier.padding(horizontal = 5.dp), color = Color(0xFF3C493F))
        }
    }
}

@Composable
fun DisplayStats(distance: Int, steps: Int) {
    Row(
        Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatsItem(label = "Distance", statsValue = distance.toString(), unit = "meters")
        StatsItem(label = "Steps", statsValue = steps.toString(), unit = "steps")
    }
}

//TODO: replace with component from map view?
@Composable
fun StatsItem(label: String, statsValue: String, unit: String) {
    Column(
        modifier = Modifier
            .size(97.dp, 80.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF3C493F))
    ) {
        Row(modifier = Modifier.padding(5.dp, 5.dp, 0.dp, 0.dp)) {
            when (label) {
                "Distance" -> {
                    Icon(
                        Icons.Rounded.NearMe,
                        "distance",
                        Modifier.size(18.dp),
                        tint = Color(0xFFCBEF43)
                    )
                }
                "Steps" -> {
                    Icon(
                        Icons.Rounded.DirectionsWalk,
                        "walking",
                        Modifier.size(18.dp),
                        tint = Color(0xFFCBEF43)
                    )
                }
            }
            Text(
                label,
                color = Color(0xFFCBEF43),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Text(
            statsValue,
            color = Color(0xFFFFFFFF),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 7.dp)
        )
        Text(unit, color = Color(0xFFFFFFFF), modifier = Modifier.padding(horizontal = 7.dp))
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


    //TODO: display something when there are no trip images saved (lottie/text/etc)

    LoadingSpinner(isDisplayed = loading.value)

    LazyRow(Modifier.padding(horizontal = 10.dp)) {
        imageBitmaps.value?.let {
            itemsIndexed(it.toList()) { _, image ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .size(270.dp, 370.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFD1CCDC)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (image != null) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            if (loading.value) {
                                LoadingSpinner(isDisplayed = loading.value)
                            } else {
                                TripPhotoItem(image)
                                Spacer(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .padding(20.dp)
                                )
                                image.note?.let { note -> TripNoteItem(note) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripPhotoItem(image: InternalStoragePhoto) {
    var isLargePhotoVisible by remember { mutableStateOf(false) }

    if (isLargePhotoVisible) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(dismissOnBackPress = false)
        ) {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier
                    .background(Color(0xFFD1CCDC))
                    .fillMaxSize()
                    .clickable {
                        isLargePhotoVisible = false
                    }) {
                Image(
                    image.bmp.asImageBitmap(), "trip photo", modifier = Modifier
                        .fillMaxSize()
                        .clickable { isLargePhotoVisible = false },
                    contentScale = ContentScale.FillWidth
                )
                Icon(
                    Icons.Rounded.Close,
                    "close",
                    Modifier
                        .size(70.dp)
                        .padding(10.dp),
                    tint = Color(0xFF3C493F)
                )
            }
        }
    } else {
        Box(contentAlignment = Alignment.TopEnd) {
            Image(
                image.bmp.asImageBitmap(), "trip photo", modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFD1CCDC))
                    .clickable { isLargePhotoVisible = true },
                contentScale = ContentScale.FillWidth
            )
            Icon(
                Icons.Rounded.OpenInFull,
                "expand",
                Modifier
                    .size(50.dp)
                    .padding(10.dp),
                tint = Color(0xFFCBEF43)
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
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}
