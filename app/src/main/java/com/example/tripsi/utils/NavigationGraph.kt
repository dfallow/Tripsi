package com.example.tripsi.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripsi.screens.currentTrip.CurrentTripView
import com.example.tripsi.screens.home.HomeView
import com.example.tripsi.screens.media.MediaView
import com.example.tripsi.screens.planTrip.PlanTripView
import com.example.tripsi.screens.travelHistory.TravelHistoryView

@Composable
fun NavigationGraph(navController: NavHostController) {
    //val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeView(navController = navController)
        }
        composable(route = Screen.TravelsScreen.route) {
            TravelHistoryView(navController = navController)
        }
        composable(route = Screen.PlanScreen.route) {
            PlanTripView(navController = navController)
        }
        composable(route = Screen.CurrentScreen.route) {
            CurrentTripView(navController = navController)

        }
        composable(route = Screen.MomentScreen.route) {

        }
        composable(route = Screen.MediaScreen.route) {
            MediaView(navController = navController)
        }
    }
}
