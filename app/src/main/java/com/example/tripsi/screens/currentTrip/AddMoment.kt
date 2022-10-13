package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
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
        MomentDetails(location, context, tripDbViewModel)
        MomentComment()
        MomentPictures(context)
        SaveOrDiscard(navController, tripDbViewModel, context)
    }
}

@Composable
fun MomentDetails(location: Location, context: Context, tripDbViewModel: TripDbViewModel) {
    // define format for date and time
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val timeFormat = SimpleDateFormat("HH:mm")
    val now = Date()

    // update location to display city name
    //location.startUpdatingLocation()
    val geoCoder = Geocoder(context, Locale.getDefault())
    val address = geoCoder.getFromLocation(
        viewModel.currentLocation.latitude,
        viewModel.currentLocation.longitude,
        1
    )
    val cityName = address[0].locality

    // Temporary for UI updating
    viewModel.momentInfo =
        CurrentTripViewModel.MomentInfo(dateFormat.format(now), timeFormat.format(now), cityName)

    //save location information to viewModel
    viewModel.momentLocation = com.example.tripsi.data.Location(
        "",
        location.userLocation.latitude,
        location.userLocation.longitude,
        dateFormat.format(now),
        tripDbViewModel.tripData.trip!!.tripId,
        MomentPosition.MIDDLE
    )

    Card(
        // Moment Information
        backgroundColor = colors.onBackground,
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

    //save comment to viewModel
    viewModel.momentNote.value = comment
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

    //directory in which photos will be stored
    val dir = context.filesDir
    //generate unique filename
    val filename = UUID.randomUUID().toString()
    val imageFile = File(dir, "${filename}.jpg")
    val photoURI = FileProvider.getUriForFile(context, "com.example.tripsi.fileprovider", imageFile)
    val currentPhotoPath = imageFile.absolutePath

    //launches the camera
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            try {
                val bmp = BitmapFactory.decodeFile(currentPhotoPath)
                //Images taken in portrait mode on some phones are always displayed incorrectly
                //This checks if the image needs to be rotated and then rotates it
                val rotatedImg = rotateImageIfRequired(bmp, currentPhotoPath)
                result.value = rotatedImg
                //image is added to the list of images that are then displayed in a LazyRow
                photoThumbnails.add(result.value)
                //save the filename to list of filenames in viewmodel
                //this is needed to later iterate through the list and save information of each image to database
                viewModel.momentImageFilenames.add(filename)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        viewModel.temporaryPhotos = photoThumbnails
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
    tripDbViewModel: TripDbViewModel,
    context: Context
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
                //check if there is at least one photo
                if (viewModel.momentImageFilenames.isNotEmpty()) {

                    //save location to database
                    viewModel.saveLocationToDb(tripDbViewModel, context)

                    Log.d("momentInfo", "${viewModel.momentInfo}")
                    viewModel.addLocationNew(
                        viewModel.momentLocation!!,
                        viewModel.momentNote.value,
                        viewModel.temporaryPhotos,
                        viewModel.momentInfo
                    )
                    viewModel.momentId.value = UUID.randomUUID().toString()

                    //save images to database
                    scope.launch {
                        val listOfFilenames = viewModel.momentImageFilenames
                        listOfFilenames.forEach {
                            viewModel.saveImageToDb(tripDbViewModel, it)
                        }
                    }

                    navController.navigateUp()
                } else {
                    Toast.makeText(context, "You must add at least one photo to save the moment.", Toast.LENGTH_LONG).show()
                }
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape
        ) {
            Text("Save")
        }
        Button(
            onClick = {
                navController.navigateUp()
            },
            modifier = viewModel.modifier,
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(colors.secondary)
        ) {
            Text("Discard")
        }
    }
}