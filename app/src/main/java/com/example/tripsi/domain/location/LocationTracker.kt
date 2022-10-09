package com.example.tripsi.domain.location

import android.location.Location

interface  LocationTracker {
    suspend fun getCurrentLocation(): Location?
}