package com.example.tripsi.screens.currentTrip

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TemporaryMoment(moment: CurrentTripViewModel.Moment) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                // Shows the image, currently set up with one mock image
                Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight(0.55f),
                shape = RoundedCornerShape(10)
            ) {

                var momentNumber by remember { mutableStateOf(1) }
                if (moment.photos != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            moment.photos[momentNumber]!!.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Buttons used to switch between images
                        if (momentNumber >= 1 && momentNumber < moment.photos.size - 1) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                TextButton(
                                    //modifier = Modifier,
                                    onClick = {
                                        momentNumber += 1
                                    }
                                ) {
                                    Icon(
                                        Icons.Rounded.ChevronRight,
                                        "arrow right",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }

                        }
                        if (momentNumber <= moment.photos.size && momentNumber > 1) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                TextButton(
                                    onClick = {
                                        momentNumber -= 1
                                    }
                                ) {
                                    Icon(
                                        Icons.Rounded.ChevronLeft, "arrow right",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }

                        }
                    }
                }
            }
            Column(
                // Contains the moment information such as date, location, time
                Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

                val style = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Date", color = MaterialTheme.colors.onSurface, style = style)
                Text(moment.info.date, color = MaterialTheme.colors.onSurface)
                Text("Time", color = MaterialTheme.colors.onSurface, style = style)
                Text(moment.info.time, color = MaterialTheme.colors.onSurface)
                Text("Location", color = MaterialTheme.colors.onSurface, style = style)
                Text(moment.info.location, color = MaterialTheme.colors.onSurface)

            }
        }
        Column(
            // This contains the moment comment and hide moment clickable text
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight(0.45f)
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 10.dp)
            ) {
                Text(
                    moment.description ?: "",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )

                ClickableText(text = AnnotatedString("Close"), onClick = {
                    viewModel.hideMoment()
                })
            }
        }
    }
}