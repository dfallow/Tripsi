package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.tripsi.R
import java.math.BigDecimal
import java.math.RoundingMode

val stepViewModel = CurrentTripViewModel()

@Composable
fun StepCounterSensor() {
    val ctx = LocalContext.current

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val stepSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


    // Variable for sensor event listener and initializing it.
    val stepSensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            event ?: return
            if (event.sensor == stepSensor) {
                stepViewModel.setSteps(event.values[0].toInt())
            }
        }
    }
    // Registering listener for our sensor manager.
    sensorManager.registerListener(
        // passing sensor event listener
        stepSensorEventListener,
        // setting sensor.
        stepSensor,
        SensorManager.SENSOR_DELAY_NORMAL
    )
    // Launch counting steps and distance
    StepCounter(stepVM = stepViewModel)
}

@Composable
fun StepCounter(
    stepVM: CurrentTripViewModel
) {
    val steps by stepVM.currentSteps.observeAsState(0)
    ShowSteps(currentSteps = steps)
    ShowDistance(currentSteps = steps)
}

@Composable
fun ShowSteps(
    currentSteps: Int
) {
    TripInfoOverlay(type = stringResource(R.string.steps), measurement = "$currentSteps")
}

@Composable
fun ShowDistance(
    currentSteps: Int
) {
    var distanceM: Int
    var distanceKm: Double

    // When it is stepped less then 100m it will show user distance in m and after in km
    if (currentSteps > 150) {
        distanceKm = (viewModel.setDistance(currentSteps).toDouble() / 1000)
        val bd = BigDecimal(distanceKm)
        val roundedDistance = bd.setScale(2, RoundingMode.DOWN)
        TripInfoOverlay(type = stringResource(R.string.distance), measurement = "$roundedDistance km")

    } else {
        distanceM = viewModel.setDistance(currentSteps)
        TripInfoOverlay(type = stringResource(R.string.distance), measurement = "$distanceM m")
    }
}
