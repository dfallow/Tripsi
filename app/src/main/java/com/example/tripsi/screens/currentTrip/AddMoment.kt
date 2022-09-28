package com.example.tripsi.screens.currentTrip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun AddMoment() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MomentDetails()
        MomentComment()
        MomentPictures()
        SaveOrDiscard()
    }
}

@Composable
fun MomentDetails() {
    Card(
        // Moment Information
        backgroundColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .padding(10.dp)
    ) {
        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {

            Column(
                // Moment Information titles
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            ) {
                Text("Date", color = MaterialTheme.colors.primary)
                Text("Time", color = MaterialTheme.colors.primary)
                Text("Location", color = MaterialTheme.colors.primary)
            }

            Column(
                // Moment Information values
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text("Some Value", color = MaterialTheme.colors.onSecondary)
                Text("Some Value", color = MaterialTheme.colors.onSecondary)
                Text("Some Value", color = MaterialTheme.colors.onSecondary)
            }
        }
    }
}

@Composable
fun MomentComment(
    colors: TextFieldColors = myAppTextFieldColors()
) {

    var comment by remember { mutableStateOf("") }

    TextField(
        value = comment,
        onValueChange = { comment = it },
        shape = RoundedCornerShape(10.dp),
        //textStyle = TextStyle(color = Color.Blue),
        label = { Text("Describe the moment...")},
        colors = colors,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(10.dp)
            .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
    )
}

@Composable
fun myAppTextFieldColors(
    textColor: Color = Color.Black,
    disabledTextColor: Color = Color.White,
    backgroundColor: Color = Color.White,
    cursorColor: Color = Color.White,
    errorCursorColor: Color = Color.White,
    focusedLabelColor: Color = Color.Black

) = TextFieldDefaults.textFieldColors(
    textColor = textColor,
    disabledTextColor = disabledTextColor,
    backgroundColor = backgroundColor,
    cursorColor = cursorColor,
    errorCursorColor = errorCursorColor,
    focusedLabelColor = focusedLabelColor,
)

@Composable
fun MomentPictures() {
    Column() {

        Row(
            // Where the taken pictures will show
        ) {

        }

        Button(
            onClick = {
            // TODO Take Picture and show in above row
            }
        ) {
            Text("Take Picture")
        }

    }
}

@Composable
fun SaveOrDiscard() {
    // Contains the buttons for saving or discarding the moment
    Row (
        horizontalArrangement = Arrangement.SpaceAround,
       verticalAlignment = Alignment.Bottom ,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
            ) {
        Button(
            onClick = {
            /*TODO*/
            }
        ) {
            Text("Save")
        }
        Button(
            onClick = {
                /*TODO*/
            }
        ) {
            Text("Discard")
        }
    }
}