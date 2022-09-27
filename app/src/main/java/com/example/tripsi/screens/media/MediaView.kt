package com.example.tripsi.screens.media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.tripsi.data.Image
import com.example.tripsi.data.Location
import com.example.tripsi.data.Note
import com.example.tripsi.data.TripData
import com.example.tripsi.functionality.TripDbViewModel

@Composable
fun DisplayHeader(tripName: String, destination: String) {
    Column() {
        Text(tripName)
        Text("Helsinki to $destination")
    }

}

@Composable
fun DisplayStats(distance: Double, duration: Double, speed: Double) {
    Row() {
        StatsItem(label = "Distance", statsValue = distance.toString(), unit = "kilometers")
        StatsItem(label = "Duration", statsValue = duration.toString(), unit = "hours")
        StatsItem(label = "Speed", statsValue = speed.toString(), unit = "km/h")
    }

}

@Composable
fun StatsItem(label: String, statsValue: String, unit: String) {
    Column() {
        Text(label)
        Text(statsValue)
        Text(unit)
    }
}

@Composable
fun MediaView(model: TripDbViewModel) {
    val tripData = model.db.tripDao().getTripData(1).observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        tripData.value?.let {
            DisplayHeader(it.trip?.tripName ?: "", it.trip?.destination ?: "")
            DisplayStats(
                distance = it.stats?.distance ?: 0.0,
                duration = it.stats?.duration ?: 0.0,
                speed = it.stats?.speed ?: 0.0
            )
            DisplayTripMediaList(tripData)
            Row() {
                Button(onClick = { /*TODO*/ }) {
                    Text("show on map")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text("create a video")
                }
            }
        }

    }
}

@Composable
fun DisplayTripMediaList(tripData: State<TripData?>) {
    val images: List<Image>? = tripData.value?.image
    val notes: List<Note>? = tripData.value?.note
    val locationData: List<Location>? = tripData.value?.location
    val imageAndNotePairs: MutableMap<String?, String?> = mutableMapOf()


    if (locationData != null) {
        for (item in locationData) {
            if (item.image != null && item.note != null) {
                val img = images!!.filter { it.imgId == item.image }
                val txt = notes!!.filter { it.noteId == item.note }
                imageAndNotePairs[img[0].filename] = txt[0].noteText
            }
            if (item.image != null && item.note == null) {
                val img = images!!.filter { it.imgId == item.image }
                imageAndNotePairs[img[0].filename] = null
            }
            if (item.image == null && item.note != null) {
                val txt = notes!!.filter { it.noteId == item.note }
                imageAndNotePairs[null] = txt[0].noteText
            }
        }
    }

    for (item in imageAndNotePairs) {
        Column() {
            Text(item.key.toString())
            Text(item.value.toString())
        }
    }


}

@Composable
fun TripPhotoItem(fileName: String) {
    Text(fileName)
}

@Composable
fun TripNoteItem(note: String) {
    Text(note)
}