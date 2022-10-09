package com.example.tripsi.screens.planTrip

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class PlanTripViewModel() : ViewModel() {

    val location = MutableStateFlow(getInitialLocation())
    val addressText = mutableStateOf("")
    var isMapEditable = mutableStateOf(true)
    private var timer: CountDownTimer? = null

    //information to be saved to database
    var tripDate: String? = null
    var tripName: String? = null
    var tripDestination: String? = null
    var tripMethod: Int? = null

    //try saving data to db
    fun saveTrip(tripDbViewModel: TripDbViewModel, context: Context): Boolean {
        var success: Boolean

        //if the user hasn't fill out all the fields, show Toast
        if (tripDate == null || tripName == null || tripDestination == null || tripMethod == null) {
            Toast.makeText(context, "Please fill out all the fields", Toast.LENGTH_LONG).show()
            success = false
        } else {
            try {
                tripDbViewModel.addTrip(
                    Trip(
                        0,
                        tripName!!,
                        tripDestination!!,
                        tripMethod!!,
                        TripStatus.UPCOMING.status,
                        tripDate!!
                    )
                )
                Toast.makeText(context, "Trip planned successfully!", Toast.LENGTH_LONG).show()
                success = true
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    "Something went wrong. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
                success = false
            }
        }
        return success
    }

    private fun getInitialLocation(): Location {
        val initialLocation = Location("")
        initialLocation.latitude = 60.1699
        initialLocation.longitude = 24.9384
        return initialLocation
    }

/*    fun updateLocation(latitude: Double, longitude: Double){
        if(latitude != location.value.latitude) {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            setLocation(location)
        }
    }

    private fun setLocation(loc: Location) {
        location.value = loc
    }*/

    fun getAddressFromLocation(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        val addressText: String

        try {
            addresses =
                geocoder.getFromLocation(location.value.latitude, location.value.longitude, 1)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val address: Address? = addresses?.get(0)
        addressText = address?.getAddressLine(0) ?: ""


        return addressText
    }

    fun onTextChanged(context: Context, text: String) {
        if (text == "")
            return
        timer?.cancel()
        timer = object : CountDownTimer(1000, 1500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                location.value = getLocationFromAddress(context, text)
            }
        }.start()
    }

    fun getLocationFromAddress(context: Context, strAddress: String): Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?

        val addresses: List<Address>? = geocoder.getFromLocationName(strAddress, 1)

        if (addresses!!.isNotEmpty()) {
            address = addresses[0]
            val loc = Location("")
            loc.latitude = address.latitude
            loc.longitude = address.longitude
            return loc
        }
        return location.value
    }

}