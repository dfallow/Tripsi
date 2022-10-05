package com.example.tripsi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.osmdroid.util.GeoPoint

class Location(context: Context): LocationListener {

    var userLocation by mutableStateOf(GeoPoint(0.0,0.0))
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun onLocationChanged(p0: Location) {
        userLocation = GeoPoint(p0.latitude, p0.longitude)
        Log.d("Location", userLocation.toString())
    }

    @SuppressLint("MissingPermission")
    fun startUpdatingLocation() {
        // TODO create checks to use Network Provider when GPS doesn't work
        val startLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        userLocation = GeoPoint(startLocation.latitude, startLocation.longitude)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 1f, this)
        Log.d("Location", startLocation.toString())
    }

    fun stopUpdatingLocation(){
        locationManager.removeUpdates(this)
    }
}