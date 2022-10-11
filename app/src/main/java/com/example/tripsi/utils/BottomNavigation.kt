package com.example.tripsi.utils

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tripsi.functionality.TripDbViewModel
import java.io.File

@Composable
fun BottomNavigation(context: Context, location: Location, tripDbViewModel: TripDbViewModel, application: Application) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        it.calculateBottomPadding()
        NavigationGraph(navController = navController, context, location, tripDbViewModel, application)

    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.TravelsScreen,
        Screen.HomeScreen,
        Screen.CurrentScreen
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination != navController.findDestination(Screen.HomeScreen.route)) {
        BottomNavigation(
            elevation = 8.dp
        ) {
            screens.forEach { screens ->
                AddItem(
                    screen = screens,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            screen.icon?.let { Icon(imageVector = it, contentDescription = "Nav icon") }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(
            alpha = ContentAlpha.disabled
        ),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )

}