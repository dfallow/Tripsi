package com.example.tripsi.screens.currentTrip

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.osmdroid.util.Distance
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
    //viewModel.reset()
    StepCounter(stepVM = stepViewModel)
    //DistanceCounter(stepVM = stepViewModel)
}

@Composable
fun ShowSteps(
    currentSteps: Int
) {
    TripInfoOverlay(type = "Steps", measurement = "$currentSteps")
}
@Composable
fun ShowDistance(
    currentSteps: Int
) {
//    var roundedDistance = String.format("%.2f", currentDistance)
//    val bd = BigDecimal(currentDistance)
//    val roundoff = bd.setScale(2, RoundingMode.DOWN)

    var distance = viewModel.setDistance(currentSteps)

    TripInfoOverlay(type = "Distance", measurement = "$distance m")
}

@Composable
fun StepCounter(stepVM: CurrentTripViewModel) {
    val steps by stepVM.currentSteps.observeAsState(0)
    ShowSteps(currentSteps = steps)
    ShowDistance(currentSteps = steps)

}

@Composable
fun ShowTime(formattedTime: String) {
    Text(text = formattedTime)
}

@Composable
fun StopWatchDisplay(
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onStartClick) {
                Text("Start")
            }
            Spacer(Modifier.width(16.dp))
            Button(onPauseClick) {
                Text("Pause")
            }
            Spacer(Modifier.width(16.dp))
            Button(onResetClick) {
                Text("Reset")
            }
        }
    }
}