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
import com.example.tripsi.utils.BottomBarScreen
import com.example.tripsi.utils.BottomNavGraph
import com.example.tripsi.utils.BottomNavigation
import com.example.tripsi.utils.Navigation

@Composable
fun HomeView()
{
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is HomeView")
        Button(onClick = {
        }) {
            Text(text = "trip history")
        }
        Button(onClick = { }) {
            Text(text = "plan a trip")
        }
    }
}