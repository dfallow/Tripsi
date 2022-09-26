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

    var userLocation by mutableStateOf(GeoPoint(60.1, 24.1))

    override fun onLocationChanged(p0: Location) {
        userLocation = GeoPoint(p0.latitude, p0.longitude)
        Log.d("Location", userLocation.toString())
    }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun startUpdatingLocation(): Location {
        val startLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100 * 1000, 100f, this)
        Log.d("Location", startLocation.toString())
        return startLocation

    }
}