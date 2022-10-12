package com.example.tripsi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.weather.WeatherViewModel
import com.example.tripsi.ui.theme.TripsiTheme
import com.example.tripsi.utils.BottomNavigation
import com.example.tripsi.utils.Location
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var tripDbViewModel: TripDbViewModel
        private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    }
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            weatherViewModel.loadWeatherInfo()
        }
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

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
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                    //Navigation(navController = NavHostController(this))


                    //for testing purposes
                    val trips = tripDbViewModel.getAllTrips().observeAsState()
                    //Text(trips.value.toString())

                    BottomNavigation(context = this, location, tripDbViewModel, weatherViewModel)

                }
            }
        }
    }
}

