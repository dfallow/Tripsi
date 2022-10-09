package com.example.tripsi.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.tripsi.domain.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import org.osmdroid.util.GeoPoint
import javax.inject.Inject
import kotlin.coroutines.resume

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


@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
): LocationTracker {

    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return null
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if(isComplete) {
                    if(isSuccessful) {
                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it)
                }
                addOnFailureListener {
                    cont.resume(null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}