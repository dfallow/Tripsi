package com.example.tripsi.utils

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.AddMoment
import com.example.tripsi.screens.currentTrip.CurrentTripView
import com.example.tripsi.screens.home.ContactDetails
import com.example.tripsi.screens.home.HomeView
import com.example.tripsi.screens.media.MediaView
import com.example.tripsi.screens.planTrip.PlanTripView
import com.example.tripsi.screens.travelHistory.TravelHistoryView
import java.io.File

@Composable
fun NavigationGraph(navController: NavHostController, context: Context, location: Location, tripDbViewModel: TripDbViewModel, application: Application) {

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeView(navController = navController, tripDbViewModel = tripDbViewModel)
        }
        composable(route = Screen.ContactScreen.route) {
            ContactDetails(navController = navController, application = application)
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
                tripDbViewModel = tripDbViewModel
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
