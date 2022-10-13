package com.example.tripsi.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = DLGreen,
    onPrimary = LGreen,
    secondary = DLGreen,
    background = Black,
    primaryVariant = DGreen,
    onSurface = PWhite,
    onBackground = DLGreen,
    secondaryVariant = LGreen

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
    secondaryVariant = LGreen






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
/*    val systemUiController = rememberSystemUiController()
    if(darkTheme){
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
    }else{
        systemUiController.setSystemBarsColor(
            color = Color.White
        )
    }*/
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

    val view = LocalView.current
    val window = (view.context as Activity).window
    window.statusBarColor = colors.onBackground.toArgb()
    window.navigationBarColor = colors.onBackground.toArgb()
}