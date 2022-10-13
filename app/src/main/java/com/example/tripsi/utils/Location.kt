package com.example.tripsi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.text.BoringLayout
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.osmdroid.util.GeoPoint

class Location(context: Context): LocationListener {

    var userLocation by mutableStateOf(GeoPoint(0.0,0.0))
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private lateinit var startLocation: Location
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun onLocationChanged(p0: Location) {
        userLocation = GeoPoint(p0.latitude, p0.longitude)
        Log.d("Location", userLocation.toString())
    }

    @SuppressLint("MissingPermission")
    fun startUpdatingLocation() {
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network information
                Log.d("error", "Location is not available")
                return
            } else {
                startLocation = if (isGPSEnabled) {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                } else {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                }
            }
        } catch (e : Exception) {
            Log.d("Error",e.message.toString())
        }


        userLocation = GeoPoint(startLocation.latitude, startLocation.longitude)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100 * 1000, 500f, this)

    }

    fun stopUpdatingLocation(){
        locationManager.removeUpdates(this)
    }
}
