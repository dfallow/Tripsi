package com.example.tripsi.screens.currentTrip

import android.content.Intent
import android.provider.MediaStore
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class CurrentTripViewModel: ViewModel() {

    var showMoment by mutableStateOf(false)

    fun displayMoment()  { showMoment = true }

    fun hideMoment() { showMoment = false }

    // Currently storing generic properties for buttons
    // TODO create own file for these properties
    val modifier = Modifier
        .width(130.dp)
    val shape = RoundedCornerShape(10.dp)

    // Temp array to show on map
    val moments = arrayOf(
        GeoPoint(60.209, 24.645),
        GeoPoint(60.259, 24.683),
        GeoPoint(60.301, 24.958)
    )

    // Variables and fun for counting steps
    val currentSteps = MutableLiveData(0)
    private val stepsBottomLine = MutableLiveData<Int>(null)

    fun setSteps(stepAmount: Int) {
        var steps = stepAmount
        if (stepsBottomLine.value == null) {
            stepsBottomLine.value = stepAmount
            steps ++
        }
        currentSteps.value = steps - (stepsBottomLine.value ?: 0)
    }


}