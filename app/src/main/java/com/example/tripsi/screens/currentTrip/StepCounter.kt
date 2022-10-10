package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext

val stepViewModel = CurrentTripViewModel()

@Composable
fun StepCounter() {
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
    CounterScreen(stepVM = stepViewModel)

}

@Composable
fun LaunchStepCounter(
    currentSteps: Int
) {
    TripInfoOverlay(type = "Steps", measurement = "$currentSteps")
}

@Composable
fun CounterScreen(stepVM: CurrentTripViewModel) {
    val steps by stepVM.currentSteps.observeAsState(0)
    LaunchStepCounter(currentSteps = steps)

}