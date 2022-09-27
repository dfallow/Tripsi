package com.example.tripsi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tripsi.data.*
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.travelHistory.TravelHistoryList
import com.example.tripsi.ui.theme.TripsiTheme

class MainActivity : ComponentActivity() {
    private lateinit var model: TripDbViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = TripDbViewModel(application)

        //addTrip(model)
        //addImage(model)
        //addNote(model)
        //addLocationData(model)
        //addStats(model)

        setContent {
            TripsiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TravelHistoryList(model)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TripsiTheme {
        Greeting("Android")
    }
}

fun addTrip(model: TripDbViewModel) {
    model.addTrip(
        Trip(
            0,
            "Weekend getaway to Paris",
            "Paris",
            TravelMethod.FLY.method,
            TripStatus.PAST.status,
            "10-11-2022"
        )
    )
    model.addTrip(
        Trip(
            0,
            "Visiting friends",
            "Berlin",
            TravelMethod.CAR.method,
            TripStatus.PAST.status,
            "20-11-2022"
        )
    )
    model.addTrip(
        Trip(
            0,
            "Hike in Vuosaari",
            "Vuosaari",
            TravelMethod.WALK.method,
            TripStatus.PAST.status,
            "10-10-2022"
        )
    )
    model.addTrip(
        Trip(
            0,
            "Biking to Turku",
            "Turku",
            TravelMethod.BIKE.method,
            TripStatus.PAST.status,
            "11-12-2022"
        )
    )
}

fun addImage(model: TripDbViewModel) {
    model.addImage(Image(0, "testfile1.jpg", 1))
    model.addImage(Image(0, "testfile11.jpg", 1))
    model.addImage(Image(0, "testfile2.jpg", 2))
    model.addImage(Image(0, "testfile31.jpg", 3))
    model.addImage(Image(0, "testfile32.jpg", 3))
    model.addImage(Image(0, "testfile33.jpg", 3))
    model.addImage(Image(0, "testfile4.jpg", 4))
}

fun addLocationData(model: TripDbViewModel) {
    model.addLocation(Location(0, 60.12, 32.19, "10-11-2022", 1, 1, 1))
    model.addLocation(Location(0, 65.12, 30.19, "10-11-2022", 2, null, 1))
    model.addLocation(Location(0, 40.12, 40.19, "20-11-2022", 3, 1, 2))
    model.addLocation(Location(0, 45.12, 45.19, "20-11-2022", 4, null, 3))
    model.addLocation(Location(0, 50.12, 50.19, "20-11-2022", 5, null, 3))
    model.addLocation(Location(0, 55.12, 55.19, "20-11-2022", 6, null, 3))
    model.addLocation(Location(0, 70.12, 70.19, "20-11-2022", 7, null, 3))
    model.addLocation(Location(0, 75.12, 75.19, "20-11-2022", null, null, 4))
}

fun addNote(model: TripDbViewModel) {
    model.addNote(Note(0, "Wow, this place is awesome!", 1))
    model.addNote(Note(0, "Amazing sunset!", 2))
}

fun addStats(model: TripDbViewModel) {
    model.updateTripStats(Statistics(1, 1, 62.4, 0.5, 101.9))
    model.updateTripStats(Statistics(1, 2, 72.4, 1.0, 105.9))
    model.updateTripStats(Statistics(1, 3, 82.4, 1.5, 110.9))
    model.updateTripStats(Statistics(1, 4, 92.4, 2.0, 115.9))
}