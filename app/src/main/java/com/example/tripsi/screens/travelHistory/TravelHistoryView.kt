package com.example.tripsi.screens.travelHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel

// TODO: cleanup the code from all the formatting, modifiers, etc

@Composable
fun TravelHistoryItem(tripName: String, imageCount: Int, noteCount: Int) {
    Row(
        modifier = Modifier
            .height(90.dp)
            .background(Color(0xFF3C493F))
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column() {
                Text(tripName, fontSize = 20.sp, color = Color(0xFFFFFFFF))
                Spacer(Modifier.size(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Row() {
                        Icon(Icons.Rounded.Image, "image icon", tint = Color(0xFFFFFFFF))
                        Spacer(Modifier.size(5.dp))
                        Text(imageCount.toString(), fontSize = 16.sp, color = Color(0xFFFFFFFF))
                    }
                    Row() {
                        Icon(Icons.Rounded.EditNote, "note icon", tint = Color(0xFFFFFFFF))
                        Spacer(Modifier.size(5.dp))
                        Text(noteCount.toString(), fontSize = 16.sp, color = Color(0xFFFFFFFF))
                    }
                    Row() {
                        Icon(Icons.Rounded.Group, "friends icon", tint = Color(0xFFFFFFFF))
                        Spacer(Modifier.size(5.dp))
                        Text("0", fontSize = 16.sp, color = Color(0xFFFFFFFF))
                    }
                }

            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.ChevronRight, "arrow right", tint = Color(0xFFCBEF43))
            }
        }
    }

}

@Composable
fun TravelHistoryList(model: TripDbViewModel) {
    val pastTrips = model.getAllTripsDataByStatus(TripStatus.PAST.status).observeAsState(listOf())

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Your trip history",
            Modifier.padding(vertical = 15.dp),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            itemsIndexed(pastTrips.value) { _, trip ->
                TravelHistoryItem(
                    tripName = trip.trip?.tripName ?: "No name",
                    imageCount = trip.image?.size ?: 0,
                    noteCount = trip.note?.size ?: 0
                )
            }
        }
    }


}