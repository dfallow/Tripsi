package com.example.tripsi

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import androidx.compose.ui.Modifier
import com.example.tripsi.ui.theme.TripsiTheme
import com.example.tripsi.utils.BottomNavigation
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.tripsi.data.*
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Location
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var tripDbViewModel: TripDbViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize database view model
        tripDbViewModel = TripDbViewModel(application)

        //for testing purposes
        /*to add data use addMockData function.
        IMPORTANT! Make sure to run it only ONCE.
        After you've built and ran the app on your phone/emulator,
        immediately comment the addMockData line out and REBUILD the app.
        If you don't do that, after you've killed the app on your phone and opened it again,
        it will duplicate some of the data.*/

        //addMockData(tripDbViewModel)


        if ((Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }

        // sets user agent allowing map to be used
        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        Configuration.getInstance().userAgentValue

        //
        val location = Location(this)

        setContent {
            TripsiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //for testing purposes
                    val trips = tripDbViewModel.getAllTrips().observeAsState()
                    //Text(trips.value.toString())

                    BottomNavigation(context = this, location, tripDbViewModel)
                }
            }
        }
    }
}


//for testing only. This will be removed later
fun addMockData(tripDbViewModel: TripDbViewModel) {
    //add PAST trips so there's something in trip history view
    tripDbViewModel.addTrip(
        Trip(
            0,
            "Visiting grandma",
            "Vilnius",
            TravelMethod.PLANE.method,
            TripStatus.PAST.status,
            "11/11/2022"
        )
    )
    tripDbViewModel.addTrip(
        Trip(
            0,
            "Weekend with friends",
            "Turku",
            TravelMethod.CAR.method,
            TripStatus.PAST.status,
            "11/12/2022"
        )
    )
    tripDbViewModel.addTrip(
        Trip(
            0,
            "Weekend getaway to Paris",
            "Paris",
            TravelMethod.PLANE.method,
            TripStatus.ACTIVE.status,
            "10/10/2022"
        )
    )
    tripDbViewModel.addTrip(
        Trip(
            0,
            "Nuuksio hike",
            "Espoo",
            TravelMethod.WALK.method,
            TripStatus.PAST.status,
            "10/2/2022"
        )
    )
    tripDbViewModel.addTrip(
        Trip(
            0,
            "Trip to Berlin",
            "Berlin",
            TravelMethod.PLANE.method,
            TripStatus.PAST.status,
            "10/5/2022"
        )
    )

    //add trip stats
    tripDbViewModel.addTripStats(Statistics(0, 1, 600.05, 3.0, 200.15))
    tripDbViewModel.addTripStats(Statistics(0, 2, 200.95, 2.0, 100.15))
    tripDbViewModel.addTripStats(Statistics(0, 3, 50.35, 1.7, 75.15))
    tripDbViewModel.addTripStats(Statistics(0, 4, 49.05, 5.0, 10.15))
    tripDbViewModel.addTripStats(Statistics(0, 5, 125.13, 1.1, 150.15))

    //add images for some trips
    tripDbViewModel.addImage(Image(0, "file1.jpg", 1))
    tripDbViewModel.addImage(Image(0, "file2.jpg", 1))
    tripDbViewModel.addImage(Image(0, "file3.jpg", 1))
    tripDbViewModel.addImage(Image(0, "file4.jpg", 3))
    tripDbViewModel.addImage(Image(0, "file5.jpg", 5))

    //add some notes
    tripDbViewModel.addNote(Note(0, "Wow what a place!", 1))
    tripDbViewModel.addNote(Note(0, "Best trip ever", 2))
    tripDbViewModel.addNote(Note(0, "Look at this sunset!", 3))

    //save some location data
    tripDbViewModel.addLocation(Location(0, 60.12, 32.19, "11/11/2022", 1, 1, 1))
    tripDbViewModel.addLocation(Location(0, 65.12, 30.19, "11/11/2022", 2, null, 1))
    tripDbViewModel.addLocation(Location(0, 65.12, 35.19, "11/11/2022", 3, null, 1))
    tripDbViewModel.addLocation(Location(0, 40.12, 40.19, "11/12/2022", null, 2, 2))
    tripDbViewModel.addLocation(Location(0, 60.200, 24.786, "10/10/2022", 4, null, 3))
    tripDbViewModel.addLocation(Location(0, 60.215, 24.647, "10/10/2022", null, 3, 3))
    tripDbViewModel.addLocation(Location(0, 60.284, 25.025, "10/10/2022", null, 3, 3))
    tripDbViewModel.addLocation(Location(0, 60.395, 25.124, "10/10/2022", null, null, 3))
    tripDbViewModel.addLocation(Location(0, 70.12, 70.19, "10/2/2022", null, null, 4))
    tripDbViewModel.addLocation(Location(0, 75.12, 75.19, "10/5/2022", 5, null, 5))
}
