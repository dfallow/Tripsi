package com.example.tripsi.data.weatherData.domain.location

import android.location.Location

interface  LocationTracker {
    suspend fun getCurrentLocation(): Location?
}