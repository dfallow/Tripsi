package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Location
import com.example.tripsi.utils.Screen
import com.example.tripsi.utils.rotateImageIfRequired
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO Important to not orientation changes clear screen


@Composable
fun AddMoment(
    navController: NavController,
    context: Context,
    location: Location,
    tripDbViewModel: TripDbViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MomentDetails(location = location, context = context, tripDbViewModel = tripDbViewModel)
        MomentComment()
        MomentPictures(context)
        SaveOrDiscard(navController, tripDbViewModel)
    }
}

@Composable
fun MomentDetails(location: Location, context: Context, tripDbViewModel: TripDbViewModel) {
    // define format for date and time
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val timeFormat = SimpleDateFormat("HH:mm")
    val now = Date()

    // update location to display city name
    location.startUpdatingLocation()
    val geoCoder = Geocoder(context, Locale.getDefault())
    val address = geoCoder.getFromLocation(
        location.userLocation.latitude,
        location.userLocation.longitude,
        1
    )
    val cityName = address[0].locality

    viewModel.momentLocation = com.example.tripsi.data.Location(
        "",
        location.userLocation.latitude,
        location.userLocation.longitude,
        dateFormat.format(now),
        tripDbViewModel.tripData.trip!!.tripId
    )

    Card(
        // Moment Information
        backgroundColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .padding(10.dp)
    ) {
        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {

            Column(
                // Moment Information titles
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            ) {
                Text("Date", color = colors.primary)
                Text("Time", color = colors.primary)
                Text("Location", color = colors.primary)
            }

            Column(
                // Moment Information values
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(dateFormat.format(now), color = colors.onSecondary)
                Text(timeFormat.format(now), color = colors.onSecondary)
                Text(
                    cityName, color = colors.onSecondary
                )
            }
        }
    }
}

@Composable
fun MomentComment(
    colors: TextFieldColors = myAppTextFieldColors()
) {

    var comment by remember { mutableStateOf("") }

    TextField(
        value = comment,
        onValueChange = { comment = it },
        shape = RoundedCornerShape(10.dp),
        //textStyle = TextStyle(color = Color.Blue),
        label = { Text("Describe the moment...") },
        colors = colors,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(10.dp)
            .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
    )
    viewModel.momentNote = comment
}

@Composable
fun myAppTextFieldColors(
    textColor: Color = Color.Black,
    disabledTextColor: Color = Color.White,
    backgroundColor: Color = Color.White,
    cursorColor: Color = Color.White,
    errorCursorColor: Color = Color.White,
    focusedLabelColor: Color = Color.Black

) = TextFieldDefaults.textFieldColors(
    textColor = textColor,
    disabledTextColor = disabledTextColor,
    backgroundColor = backgroundColor,
    cursorColor = cursorColor,
    errorCursorColor = errorCursorColor,
    focusedLabelColor = focusedLabelColor,
)

@Composable
fun MomentPictures(context: Context) {
    val photoThumbnails = remember { mutableListOf<Bitmap?>(null) }
    val result = remember { mutableStateOf<Bitmap?>(null) }

    val dir = context.filesDir
    val filename = UUID.randomUUID().toString()
    val imageFile = File(dir, "${filename}.jpg")
    val photoURI = FileProvider.getUriForFile(context, "com.example.tripsi.fileprovider", imageFile)
    val currentPhotoPath = imageFile.absolutePath

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            try {
                val bmp = BitmapFactory.decodeFile(currentPhotoPath)
                val rotatedImg = rotateImageIfRequired(bmp, currentPhotoPath)
                result.value = rotatedImg
                photoThumbnails.add(result.value)
                viewModel.momentImageFilenames.add(filename)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            Log.d("GIGI", "Picture not taken")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyRow(
            modifier = Modifier.fillMaxHeight(0.35f)
        ) {
            itemsIndexed(photoThumbnails) { _, item ->
                if (item != null) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .size(150.dp, 150.dp)
                            .padding(10.dp)
                    ) {
                        Image(
                            item.asImageBitmap(),
                            null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxSize()

                        )
                    }

                }
            }
        }

        // TODO If I remove this the photos do not appear in the above lazyRow
        Row {
            result.value?.let {
                //TODO
            }
        }

        val scope = rememberCoroutineScope()
        // TODO Replace button with some kind of camera clickable icon
        Button(
            onClick = {
                scope.launch {
                    launcher.launch(photoURI)
                }
            }
        ) {
            Text("Take Photo")
        }

    }
}

@Composable
fun SaveOrDiscard(
    navController: NavController,
    tripDbViewModel: TripDbViewModel
) {

    val scope = rememberCoroutineScope()
    // Contains the buttons for saving or discarding the moment
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        Button(
            onClick = {
                viewModel.saveLocationToDb(tripDbViewModel)
                //viewModel.saveNoteToDb(tripDbViewModel)

                scope.launch {
                    val listOfFilenames = viewModel.momentImageFilenames
                    listOfFilenames.forEach {
                        viewModel.saveImageToDb(tripDbViewModel, it)
                    }
                    //TODO shouldn't use delay
                    delay(1000)
                    viewModel.clearData()
                }
                navController.navigate(Screen.CurrentScreen.route)
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape
        ) {
            Text("Save")
        }
        Button(
            onClick = {
                /*TODO finish functionality*/
                navController.navigate(Screen.CurrentScreen.route)
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(colors.secondary)
        ) {
            Text("Discard")
        }
    }
}