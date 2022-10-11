package com.example.tripsi.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class StopWatch {

    var formattedTime by mutableStateOf("00:00:00")

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isStarted = false

    private var timeMillis = 0L
    private var lastTimestamp = 0L

    fun start() {
        if(isStarted) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            isStarted = true
            while(isStarted) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = formatTime(timeMillis - 7200000)
            }
        }
    }

    fun pause() {
        isStarted = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimestamp = 0L
        formattedTime = "00:00:00"
        isStarted = false
    }

    private fun formatTime(timeMillis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "HH:mm:ss",
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }

}