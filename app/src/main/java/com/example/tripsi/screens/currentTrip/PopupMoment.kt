package com.example.tripsi.screens.currentTrip

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp


@Composable
fun PopupMoment(imageId: Int, moment: CurrentTripViewModel.Moment) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.White)
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
                var momentNumber by remember { mutableStateOf(0) }
                //Image(painter = painterResource(imageId), contentDescription = "something")
                if (moment.photos != null) {
                    Box(modifier = Modifier.fillMaxSize()){
                        Image(
                            moment.photos[momentNumber]!!.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Buttons used to switch between images
                        Log.d("momentNumberMax", moment.photos.size.toString())
                        if (momentNumber >= 0 && momentNumber < moment.photos.size - 1) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Button(
                                    //modifier = Modifier,
                                    onClick = {
                                        momentNumber += 1
                                    }
                                ) {

                                }
                            }

                        }
                        if (momentNumber <= moment.photos.size && momentNumber > 0) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Button(
                                    //modifier = Modifier,
                                    onClick = {
                                        momentNumber -= 1
                                    }
                                ) {

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
                    .padding(horizontal = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(viewModel.momentInfo.date, color = Color.Black)
                Text(viewModel.momentInfo.time)
                Text(viewModel.momentInfo.location)

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
                Text(moment.description ?: "")

                ClickableText(text = AnnotatedString("Close"), onClick = {
                    viewModel.hideMoment()
                })
            }
        }
    }
}