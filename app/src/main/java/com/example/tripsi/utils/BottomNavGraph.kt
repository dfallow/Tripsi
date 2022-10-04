package com.example.tripsi.utils


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripsi.screens.currentTrip.CurrentTripView
import com.example.tripsi.screens.home.HomeView
import com.example.tripsi.screens.planTrip.PlanTripView
import com.example.tripsi.screens.travelHistory.TravelHistoryView

/*@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.TravelsScreen.route) {
            TravelHistoryView(navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeView(navController)
        }
        composable(route = Screen.CurrentScreen.route) {
            CurrentTripView(navController)
        }
    }
}*/
