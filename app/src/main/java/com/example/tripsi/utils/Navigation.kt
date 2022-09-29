package com.example.tripsi.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.CurrentTripView
import com.example.tripsi.screens.home.HomeView
import com.example.tripsi.screens.media.MediaView
import com.example.tripsi.screens.planTrip.PlanTripView
import com.example.tripsi.screens.travelHistory.TravelHistoryView

@Composable
fun Navigation(context: Context, location: Location, model: TripDbViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeView(navController = navController)
        }
        composable(route = Screen.TravelsScreen.route) {
            TravelHistoryView(navController = navController, model = model)
        }
        composable(route = Screen.PlanScreen.route) {
            PlanTripView(navController = navController)
        }
        composable(route = Screen.CurrentScreen.route) {
            CurrentTripView(navController = navController, context = context, location = location
            )
        }
        composable(route = Screen.MediaScreen.route) {
            MediaView(navController = navController, model = model, tripId = model.tripId)
        }

    }
}