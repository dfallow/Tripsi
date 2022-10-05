package com.example.tripsi.utils

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object PlanScreen : Screen("plan")
    object CurrentScreen : Screen("current")
    object TravelsScreen : Screen("travel")
    object MediaScreen : Screen("media")
    object MomentScreen : Screen("moment")
}