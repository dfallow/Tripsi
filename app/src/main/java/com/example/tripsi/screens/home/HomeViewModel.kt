package com.example.tripsi.screens.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.tripsi.data.Trip
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import java.text.SimpleDateFormat
import java.util.*


class HomeViewModel() : ViewModel() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    private val now = Date()

    fun startQuickTrip(tripDbViewModel: TripDbViewModel, context: Context, name: String?): Boolean {
        var success:Boolean
        try {
            tripDbViewModel.addTrip(
                Trip(
                    0,
                    name ?: "Quick trip",
                    "Unknown",
                    1,
                    TripStatus.UPCOMING.status,
                    dateFormat.format(now)
                )
            )
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
        return success
    }
}