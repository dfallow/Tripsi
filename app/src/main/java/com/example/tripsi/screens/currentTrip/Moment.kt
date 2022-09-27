package com.example.tripsi.screens.currentTrip

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
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(MaterialTheme.colors.onBackground)
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Card(
                Modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.55f),
                shape = RoundedCornerShape(10)
            ) {
                Image(painter = painterResource(imageId), contentDescription = "something")
            }


            Column(
                Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .background(Color.Black),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Date")
                Text("Time")
                Text("Location")

            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            ClickableText(text = AnnotatedString("Close"), onClick = {
                viewModel.hideMoment()
            })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}