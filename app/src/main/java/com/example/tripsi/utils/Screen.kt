package com.example.tripsi.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector?
) {
    object HomeScreen : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object ContactScreen : Screen(
        route = "contact",
        title = "Contact",
        icon = null
    )

    object PlanScreen : Screen(
        route = "plan",
        title = "Plan",
        icon = null
    )

    object CurrentScreen : Screen(
        route = "current",
        title = "Current",
        icon = Icons.Default.LocationOn
    )

    object TravelsScreen : Screen(
        route = "travels",
        title = "Travels",
        icon = Icons.Default.Menu
    )

    object MediaScreen : Screen(
        route = "media",
        title = "Media",
        icon = null
    )

    object MomentScreen : Screen(
        route = "moment",
        title = "Moment",
        icon = null
    )
}