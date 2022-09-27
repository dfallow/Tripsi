package com.example.tripsi.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripsi.screens.home.HomeView
import com.example.tripsi.screens.planTrip.PlanTripView
import com.example.tripsi.screens.travelHistory.TravelHistoryView

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Map.route
    ) {
        composable(route = BottomBarScreen.Trips.route) {
            TravelHistoryView()
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeView()
        }
        composable(route = BottomBarScreen.Map.route) {
            PlanTripView()
        }
    }
}