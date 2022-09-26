package com.example.tripsi.screens.media

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MediaView(navigator: DestinationsNavigator) {
    Text(text = "This is MediaView")
}