package com.example.tripsi.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = DLGreen,
    onPrimary = LGreen,
    secondary = DLGreen,
    background = Black,
    primaryVariant = DGreen,
    onSurface = PWhite,
    onBackground = DLGreen,
    secondaryVariant = LGreen,
    error = Red

)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = LGreen,
    onPrimary = DGreen,
    secondary = DPurple,
    background = White,
    primaryVariant = DGreen,
    onSurface = PWhite,
    onBackground = DLGreen,
    secondaryVariant = LGreen,
    error = DPurple

)

@Composable
fun TripsiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    val window = (view.context as Activity).window
    window.statusBarColor = colors.primaryVariant.toArgb()
    window.navigationBarColor = colors.primaryVariant.toArgb()
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

}