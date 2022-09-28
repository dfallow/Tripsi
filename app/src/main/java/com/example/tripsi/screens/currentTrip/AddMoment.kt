package com.example.tripsi.screens.currentTrip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddMoment() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MomentDetails()
    }
}

@Composable
fun MomentDetails() {
    Card(
        // Moment Information
        backgroundColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .padding(10.dp)
    ) {
        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {

            Column(
                // Moment Information titles
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            ) {
                Text("Date", color = MaterialTheme.colors.primary)
                Text("Time", color = MaterialTheme.colors.primary)
                Text("Location", color = MaterialTheme.colors.primary)
            }

            Column(
                // Moment Information values
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text("Some Value", color = MaterialTheme.colors.onSecondary)
                Text("Some Value", color = MaterialTheme.colors.onSecondary)
                Text("Some Value", color = MaterialTheme.colors.onSecondary)
            }
        }
    }
}