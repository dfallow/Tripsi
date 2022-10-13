package com.example.tripsi.screens.pastTrip

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PastTripViewModel {

    lateinit var currentMomentId: String

    var showMoment by mutableStateOf(false)
    fun displayMoment() { showMoment = true }
    fun hideMoment() { showMoment = false }

}