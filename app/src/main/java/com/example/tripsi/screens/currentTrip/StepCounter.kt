package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val stepViewModel = CurrentTripViewModel()


@Composable
fun StepCounter() {
    val ctx = LocalContext.current

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val stepSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


    // on below line we are creating a variable
    // for sensor event listener and initializing it.
    val stepSensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // method to check accuracy changed in sensor.
        }

        // on below line we are creating a sensor on sensor changed
        override fun onSensorChanged(event: SensorEvent) {
            event ?: return
            if (event.sensor == stepSensor) {
                stepViewModel.setSteps(event.values[0].toInt())
            }
        }
    }
    // on below line we are registering listener for our sensor manager.
    sensorManager.registerListener(
        // on below line we are passing
        // proximity sensor event listener
        stepSensorEventListener,

        // on below line we are
        // setting proximity sensor.
        stepSensor,

        // on below line we are specifying
        // sensor manager as delay normal
        SensorManager.SENSOR_DELAY_NORMAL
    )
    CounterScreen(stepVM = stepViewModel)

}

@Composable
fun LaunchStepCounter(
    currentSteps: Int,
    isRunning: Boolean,
    onStart: () -> Unit
) {
    /*Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        //onStart()
        Text(
            text = if (!isRunning) "$currentSteps" else "nothing",
        )
    }*/
    Text(
        text = if (!isRunning) "$currentSteps" else "nothing"
    )
}

@Composable
fun CounterScreen(stepVM: CurrentTripViewModel) {
    val steps by stepVM.currentSteps.observeAsState(0)
    val isRunning by stepVM.isRunning.observeAsState(null)

   LaunchStepCounter(currentSteps = steps, isRunning == true, onStart = {
        Log.d("msg", "LaunchStepCounter")
        //stepVM.reset()
    })

}