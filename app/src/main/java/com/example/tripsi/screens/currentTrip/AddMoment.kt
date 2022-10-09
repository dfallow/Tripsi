package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import com.example.tripsi.utils.Location
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.List
import androidx.navigation.NavController
import com.example.tripsi.utils.Screen

// TODO Important to not orientation changes clear screen

@Composable
fun AddMoment(navController: NavController, context: Context, location: Location) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MomentDetails(location = location, context = context)
        MomentComment()
        MomentPictures()
        SaveOrDiscard(navController, context, location)
    }
}

@Composable
fun MomentDetails(location: Location, context: Context) {
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
        onValueChange = {
            comment = it
            viewModel.momentComment.value = comment
                        },
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
fun MomentPictures() {

    val photoThumbnails = remember { mutableListOf<Bitmap?>(null) }

    val result = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        result.value = it
        Log.d("photo", "test")
        photoThumbnails.add(it)
        Log.d("photo", photoThumbnails.toString())
        viewModel.momentPhotos.add(it)
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
        Row() {
            result.value?.let { }
        }

        // TODO Replace button with some kind of camera clickable icon
        Button(
            onClick = {
                Log.d("photo", "test")
                launcher.launch()
            }
        ) {
            Text("Take Photo")
        }

    }
}

@Composable
fun SaveOrDiscard(navController: NavController, context: Context, location: Location) {
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
            /*TODO*/
                      viewModel.addLocation(
                          location = location,
                          description = viewModel.momentComment.value,
                          photos = viewModel.momentPhotos
                      )
                navController.navigateUp()
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