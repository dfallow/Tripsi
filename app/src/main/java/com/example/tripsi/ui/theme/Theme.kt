package com.example.tripsi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = darkGreen
)

private val LightColorPalette = lightColors(
    // Generic Buttons
    primary = genericBtnLight,
    onPrimary = genericBtnTextLight,

    // Cancel Buttons
    secondary = cancelBtnLight,
    onSecondary = cancelBtnTextLight,

    primaryVariant = Purple700,
<<<<<<< HEAD
    secondary = darkGreen
=======
    onBackground = navBar,


>>>>>>> master

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TripsiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}