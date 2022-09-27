package com.example.tripsi.utils

import android.icu.text.CaseMap.Title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Trips : BottomBarScreen(
        route = "trips",
        title = "Trips",
        icon = Icons.Default.Menu
    )
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Map : BottomBarScreen(
        route = "map",
        title = "Map",
        icon = Icons.Default.LocationOn
    )
}
