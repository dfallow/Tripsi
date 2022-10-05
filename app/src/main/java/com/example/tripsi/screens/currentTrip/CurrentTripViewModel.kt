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
import androidx.navigation.NavController
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripStatus
import com.example.tripsi.utils.Location
import org.osmdroid.util.GeoPoint

class CurrentTripViewModel: ViewModel() {

    // Temporary status of trip, used to update UI
    var currentStatus by mutableStateOf(TripStatus.UPCOMING.status)
    fun startActive() { currentStatus = TripStatus.ACTIVE.status }
    fun endActive() { currentStatus = TripStatus.PAST.status }

    var showMoment by mutableStateOf(false)
    fun displayMoment()  { showMoment = true }
    fun hideMoment() { showMoment = false }

    // Used to update UI when a moment is added
    // TODO find a better way
    var showText by mutableStateOf(false)
    fun toggleText() { showText = !showText }

    // Array of GeoPoints for ViewModel
    val currentTripMoments: MutableLiveData<ArrayList<GeoPoint>> by lazy {
        MutableLiveData<ArrayList<GeoPoint>>()
    }

    // value is just used to update the MutableLiveData object
    private val tempMomentArray = ArrayList<GeoPoint>()

    // Adds the users current location as the start location, for temporary UI changes
    fun addStartLocation(location: Location) {
        tempMomentArray.clear()
        tempMomentArray += GeoPoint(location.userLocation.latitude, location.userLocation.longitude)
        currentTripMoments.value = tempMomentArray
    }

    // Used for adding moments and endLocation to UI temporarily
    // TODO Create an object that holds both GeoPoint and Boolean?
    fun addLocation(location: Location, isEnd: Boolean) {
        tempMomentArray += GeoPoint(location.userLocation.latitude, location.userLocation.longitude)
        currentTripMoments.value = tempMomentArray
    }

    // clears temporary value and resets the currentStatus, also removes location updates
    fun goHome(location: Location) {
        tempMomentArray.clear()
        currentTripMoments.value = tempMomentArray

        // This won't affect UI in future as we will navigate to another screen
        currentStatus = TripStatus.UPCOMING.status

        location.stopUpdatingLocation()
    }

    // Currently storing generic properties for buttons
    // TODO create own file for these properties
    val modifier = Modifier
        .width(130.dp)
    val shape = RoundedCornerShape(10.dp)

}