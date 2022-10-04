package com.example.tripsi.screens.media


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripsi.functionality.TripDbViewModel

@Composable
fun MediaView(tripDbViewModel: TripDbViewModel, tripId: Int, navController: NavController, context: Context) {
    val tripData = tripDbViewModel.getTripData(tripId).observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tripData.value?.let {
            DisplayTitle(it.trip?.tripName ?: "")
            DisplayRoute(it.trip?.destination ?: "")
            DisplayStats(
                distance = it.stats?.distance ?: 0.0,
                duration = it.stats?.duration ?: 0.0,
                speed = it.stats?.speed ?: 0.0
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                DisplayTripMediaList(it.trip!!.tripId, tripDbViewModel)
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
                                    /*TODO*/
                                    Toast.makeText(context, "Nothing yet...", Toast.LENGTH_LONG).show()
                                  },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFCBEF43),
                            contentColor = Color(0xFF2D0320)
                        )
                    ) {
                        Text("create a video")
                    }
                }
            }
        }
    }
}

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

@Composable
fun DisplayRoute(destination: String) {
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
            Text("Helsinki", Modifier.padding(horizontal = 5.dp), color = Color(0xFF3C493F))
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
            Text(destination, Modifier.padding(horizontal = 5.dp), color = Color(0xFF3C493F))
        }
    }
}

@Composable
fun DisplayStats(distance: Double, duration: Double, speed: Double) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatsItem(label = "Distance", statsValue = distance.toString(), unit = "kilometers")
        StatsItem(label = "Time", statsValue = duration.toString(), unit = "hours")
        StatsItem(label = "Speed", statsValue = speed.toString(), unit = "km/h")
    }
}

//TODO: replace with component from map view
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
                "Time" -> {
                    Icon(
                        Icons.Rounded.Schedule,
                        "time",
                        Modifier.size(18.dp),
                        tint = Color(0xFFCBEF43)
                    )
                }
                "Speed" -> {
                    Icon(
                        Icons.Rounded.Speed,
                        "speed",
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

@Composable
fun DisplayTripMediaList(tripId: Int, tripDbViewModel: TripDbViewModel) {
    //this retrieves all coordinates saved for the trip
    val tripMedia = tripDbViewModel.getTripLocationData(tripId).observeAsState(listOf())

    LazyRow(Modifier.padding(horizontal = 10.dp)) {
        itemsIndexed(tripMedia.value) { _, item ->
            //if imageId/noteId is associated with the coordinates, retrieve all Image and Note data by their ids
            val img = item.image?.let { tripDbViewModel.getImageById(it).observeAsState() }
            val txt = item.note?.let { tripDbViewModel.getNoteById(it).observeAsState() }

            //if there is at least one image, display the card
            if (img != null) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .size(270.dp, 370.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFD1CCDC)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        TripPhotoItem(img.value?.filename.toString())
                        Spacer(
                            modifier = Modifier
                                .size(10.dp)
                                .padding(20.dp)
                        )
                        if (txt != null) {
                            TripNoteItem(txt.value?.noteText.toString())
                        }
                    }
                }
                // if there's only a note but no image, display just the note
            } else if (txt != null) {
                Column(
                    Modifier
                        .padding(15.dp)
                        .size(270.dp, 370.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TripNoteItem(txt.value?.noteText.toString())

                }
            }
        }
    }
}

//TODO: display actual image
@Composable
fun TripPhotoItem(fileName: String) {
    Text(
        fileName,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .size(250.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Gray)
    )
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