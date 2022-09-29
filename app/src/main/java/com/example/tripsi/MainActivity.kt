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
import com.example.tripsi.utils.Navigation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Location
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var model: TripDbViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize database view model
        model = TripDbViewModel(application)

        if ((Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            0)
            }

        // sets user agent allowing map to be used
        Configuration.getInstance().load(this,
            PreferenceManager.getDefaultSharedPreferences(this))
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
                    Navigation(context = this, location, model)

                    //Text("Hello there")
                    //CurrentTripView(location, this, application)
                    //AddMoment()
                    //PlanTrip()
                }
            }
        }
    }
}
