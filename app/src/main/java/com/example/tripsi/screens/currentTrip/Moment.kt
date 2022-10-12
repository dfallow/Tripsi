package com.example.tripsi.screens.currentTrip

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp


@Composable
fun PopupMoment(imageId: Int) {
    Log.d("currentMoment", viewModel.currentMomentId)
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
                Image(painter = painterResource(imageId), contentDescription = "something")
            }
            Column(
                // Contains the moment information such as date, location, time
                Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Date", color = Color.Black)
                Text("Time")
                Text("Location")

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
                Text("This is a temporary string, which will be replaced with the moment comment")

                ClickableText(text = AnnotatedString("Close"), onClick = {
                    viewModel.hideMoment()
                })
            }
        }
    }
}