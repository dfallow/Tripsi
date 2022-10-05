package com.example.tripsi.screens.currentTrip

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsi.utils.Location
import org.osmdroid.util.GeoPoint

class CurrentTripViewModel: ViewModel() {

    var showMoment by mutableStateOf(false)


    // Array of GeoPoints for ViewModel
    val currentTripMoments: MutableLiveData<ArrayList<GeoPoint>> by lazy {
        MutableLiveData<ArrayList<GeoPoint>>()
    }

    private val tempMomentArray = ArrayList<GeoPoint>()

    // Adds the users current location as the start location
    fun addStartLocation(location: Location) {
        tempMomentArray.clear()
        tempMomentArray += GeoPoint(location.userLocation.latitude, location.userLocation.longitude)

        currentTripMoments.value = tempMomentArray
    }

    fun displayMoment()  { showMoment = true }

    fun hideMoment() { showMoment = false }

    // Currently storing generic properties for buttons
    // TODO create own file for these properties
    val modifier = Modifier
        .width(130.dp)
    val shape = RoundedCornerShape(10.dp)

    // Temp array to show on map
    val moments = arrayOf(
        GeoPoint(60.209, 24.645),
        GeoPoint(60.259, 24.683),
        GeoPoint(60.301, 24.958)
    )

}