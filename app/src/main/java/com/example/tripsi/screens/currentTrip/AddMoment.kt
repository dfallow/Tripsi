package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.tripsi.R
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Location
import com.example.tripsi.utils.rotateImageIfRequired
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


val imagesAndFilenames: MutableList<ArrayMap<Bitmap, String>> = mutableListOf()

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
        backgroundColor = colors.primaryVariant,
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
                Text("Date", color = colors.onSurface)
                Text("Time", color = colors.onSurface)
                Text("Location", color = colors.onSurface)
            }

            Column(
                // Moment Information values
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(dateFormat.format(now), color = colors.onSurface)
                Text(timeFormat.format(now), color = colors.onSurface)
                Text(cityName, color = colors.onSurface)
            }
        }
    }
}

@Composable
fun MomentComment(
    colors: TextFieldColors = myAppTextFieldColors()
) {

    var comment by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        value = comment,
        onValueChange = { comment = it },
        shape = RoundedCornerShape(10.dp),
        label = { Text("Describe the moment...", color = MaterialTheme.colors.primaryVariant) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
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
    backgroundColor: Color = colors.onSurface,
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
    val photoThumbnails = remember { mutableStateListOf<Bitmap?>(null) }
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
                imagesAndFilenames.add(arrayMapOf(Pair(rotatedImg, filename)))
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
                        Box(contentAlignment = Alignment.TopEnd) {
                            Image(
                                item.asImageBitmap(),
                                null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            Icon(
                                Icons.Rounded.Delete,
                                "delete",
                                Modifier
                                    .size(50.dp)
                                    .padding(10.dp)
                                    .clickable {
                                        imagesAndFilenames.forEach { pair ->
                                            pair.forEach { (image, filename) ->
                                                if (image == item) {
                                                    val files = context.filesDir.listFiles()
                                                    val file = files?.first {
                                                        it.canRead() && it.isFile &&
                                                                it.name.equals("$filename.jpg")
                                                    }
                                                    try {
                                                        file?.delete()
                                                        viewModel.momentImageFilenames.remove(
                                                            filename
                                                        )
                                                        photoThumbnails.remove(image)
                                                    } catch (e: IOException) {
                                                        e.printStackTrace()
                                                    }
                                                }
                                            }
                                        }
                                    },
                                tint = Color(0xFFCBEF43)
                            )
                        }

                    }

                }
            }
        }

        Row {
            //do not remove this
            result.value?.let {}
        }

        val scope = rememberCoroutineScope()
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.camera_add_svgrepo_com),
            tint = colors.onPrimary,
            contentDescription = "",
            modifier = Modifier
                .clickable {
                    scope.launch {
                        launcher.launch(photoURI)
                    }
                },
        )

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
                    Toast.makeText(
                        context,
                        "You must add at least one photo to save the moment.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .width(130.dp)
                .height(45.dp),
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.primary,
                contentColor = colors.onPrimary
            ),
        ) {
            Text("Save")
        }
        Button(
            onClick = {
                navController.navigateUp()
            },
            modifier = Modifier
                .width(100.dp)
                .height(35.dp),
            shape = viewModel.shape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.error,
                contentColor = colors.onSurface
            ),
        ) {
            Text("Discard")
        }
    }
}