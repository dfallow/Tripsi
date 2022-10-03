package com.example.tripsi.screens.home

import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.tripsi.utils.*

@Composable
fun HomeView(navController: NavController)
{
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is HomeView")
        Button(onClick = {
            navController.navigate(Screen.TravelsScreen.route)
        }) {
            Text(text = "trip history")
        }
        Button(onClick = {
            navController.navigate(Screen.PlanScreen.route)
        }) {
            Text(text = "plan a trip")
        }
        Button(onClick = {
            navController.navigate(Screen.MediaScreen.route)
        }) {
            Text(text = "Start a trip")
        }
    }
}