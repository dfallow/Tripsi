package com.example.tripsi.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.AddMoment
import com.example.tripsi.screens.currentTrip.CurrentTripView
import com.example.tripsi.screens.home.HomeView
import com.example.tripsi.screens.media.MediaView
import com.example.tripsi.screens.planTrip.PlanTripView
import com.example.tripsi.screens.travelHistory.TravelHistoryView
import com.example.tripsi.screens.weather.WeatherViewModel
import java.io.File

@Composable
fun NavigationGraph(navController: NavHostController, context: Context, location: Location, tripDbViewModel: TripDbViewModel, weatherViewModel: WeatherViewModel) {

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeView(
                navController = navController,
                tripDbViewModel = tripDbViewModel,
                context = context,
                weatherViewModel
                )
        }
        composable(route = Screen.TravelsScreen.route) {
            TravelHistoryView(navController = navController, tripDbViewModel = tripDbViewModel)
        }
        composable(route = Screen.PlanScreen.route) {
            PlanTripView(navController = navController, tripDbViewModel = tripDbViewModel)
        }
        composable(route = Screen.CurrentScreen.route) {
            CurrentTripView(
                navController = navController,
                context = context,
                location = location,
                tripDbViewModel = tripDbViewModel,
                weatherViewModel = weatherViewModel
            )
        }
        composable(route = Screen.MomentScreen.route) {
            AddMoment(navController = navController, context = context, location = location, tripDbViewModel = tripDbViewModel)
        }
        composable(route = Screen.MediaScreen.route) {
            MediaView(navController = navController, tripDbViewModel = tripDbViewModel, tripId = tripDbViewModel.tripId, context = context)
        }

    }
}
