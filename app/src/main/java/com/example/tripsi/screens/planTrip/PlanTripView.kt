@file:Suppress("NAME_SHADOWING")

package com.example.tripsi.screens.planTrip

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tripsi.R
import com.example.tripsi.data.TravelMethod
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Screen
import java.util.*

@Composable
fun PlanTripView(navController: NavController, tripDbViewModel: TripDbViewModel) {
    val planTripViewModel = PlanTripViewModel()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround

    ) {
        Text(
            text = "Create a plan for your upcoming trip",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        DatePicker(planTripViewModel)
        TextInput(planTripViewModel)
        MapAddressPickerView(planTripViewModel)
        TripType(planTripViewModel)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    val saved = planTripViewModel.saveTrip(tripDbViewModel, context)
                    if (saved) navController.navigate(Screen.HomeScreen.route)
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFCBEF43))
            ) {
                Text(
                    text = "Save", fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2D0320)
                )
            }
            Button(
                onClick = { /*TODO Navigate Back to homeView*/ },
                colors = ButtonDefaults.buttonColors(Color(0xFF200217))
            ) {
                Text(
                    text = "Discard",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFBC02D)
                )
            }
        }
    }
}

@Composable
fun TextInput(planTripViewModel: PlanTripViewModel) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            planTripViewModel.tripName = it
            text = it
        },
        label = { Text("Name of your trip", color = Color(0xFF2D0320)) },
        modifier = Modifier
            .fillMaxWidth(),

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFFCBEF43),
            unfocusedBorderColor = Color(0xFF3C493F)
        )
    )
}

@Composable
fun MapAddressPickerView(planTripViewModel: PlanTripViewModel) {
    Surface {
        val currentLocation = planTripViewModel.location.collectAsState()
        var text by remember { planTripViewModel.addressText }
        val context = LocalContext.current


        Row {

            currentLocation.value.let {
                if (planTripViewModel.isMapEditable.value) {
                    text = planTripViewModel.getAddressFromLocation(context)
                }
            }
        }
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        planTripViewModel.tripDestination = it
                        text = it
                        if (!planTripViewModel.isMapEditable.value)
                            planTripViewModel.onTextChanged(context, text)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "clear text",
                            modifier = Modifier.clickable { text = "" })

                    },
                    label = { Text("What's your final destination", color = Color(0xFF2D0320)) },

                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !planTripViewModel.isMapEditable.value,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFCBEF43),
                        unfocusedBorderColor = Color(0xFF3C493F)
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) {
                Button(
                    onClick = {
                        planTripViewModel.isMapEditable.value =
                            !planTripViewModel.isMapEditable.value
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFCBEF43))
                ) {
                    Text(
                        text = if (planTripViewModel.isMapEditable.value) "Insert" else "Save",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2D0320)
                    )

                }
            }
        }

    }
}


@Composable
fun DatePicker(planTripViewModel: PlanTripViewModel) {

    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            planTripViewModel.tripDate = mDate.value
        }, mYear, mMonth, mDay
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color(0xFFD1CCDC)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    )

    {

        // Creating a button that on
        // click displays/shows the DatePickerDialog
        Button(
            onClick = {
                mDatePickerDialog.show()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3C493F))
        )
        {
            Icon(
                painter = painterResource(R.drawable.calendar), contentDescription = "Calendar",
                tint = Color(0xFFCBEF43)
            )
            //  Text(text = "Date", color = Color(0xFF2D0320))
        }

        // Adding a space of 100dp height
        Spacer(modifier = Modifier.size(10.dp))

        // Displaying the mDate value in the Text
        Text(
            text = "Trip Date: ${mDate.value}",
            Modifier.padding(10.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.Center

        )
    }
}

@Composable
fun TripType(planTripViewModel: PlanTripViewModel) {
    val selectedValue = remember { mutableStateOf("") }
    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
    val onChangeState: (String) -> Unit = { selectedValue.value = it }

    val items = listOf(
        TravelMethod.CAR,
        TravelMethod.BUS,
        TravelMethod.BIKE,
        TravelMethod.WALK,
        TravelMethod.PLANE
    )

    when (selectedValue.value) {
        TravelMethod.CAR.name -> {
            planTripViewModel.tripMethod = TravelMethod.CAR.method
        }
        TravelMethod.BUS.name -> {
            planTripViewModel.tripMethod = TravelMethod.BUS.method
        }
        TravelMethod.BIKE.name -> {
            planTripViewModel.tripMethod = TravelMethod.BIKE.method
        }
        TravelMethod.WALK.name -> {
            planTripViewModel.tripMethod = TravelMethod.WALK.method
        }
        TravelMethod.PLANE.name -> {
            planTripViewModel.tripMethod = TravelMethod.PLANE.method
        }
    }

    Text(text = "Selected Type of trip: ${selectedValue.value.ifEmpty { "NONE" }}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3C493F)),
        horizontalArrangement = Arrangement.SpaceEvenly

    )
    {
        items.forEach { item ->

            IconToggleButton(checked = isSelectedItem(item.name),
                onCheckedChange = { onChangeState(item.name) }
            )
            {
                when (item) {
                    TravelMethod.CAR -> Icon(
                        painter = painterResource(if (isSelectedItem.invoke(item.name)) R.drawable.unselected_car else R.drawable.car),
                        contentDescription = "car",
                        tint = Color(0xFFCBEF43)
                    )
                    TravelMethod.BUS -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_bus else R.drawable.bus),
                        contentDescription = "bus",
                        tint = Color(0xFFCBEF43)
                    )
                    TravelMethod.BIKE -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_bike else R.drawable.bike),
                        contentDescription = "bike",
                        tint = Color(0xFFCBEF43)
                    )
                    TravelMethod.WALK -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_walk else R.drawable.walk),
                        contentDescription = "walk",
                        tint = Color(0xFFCBEF43)
                    )
                    TravelMethod.PLANE -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_plane else R.drawable.plane),
                        contentDescription = "plane",
                        tint = Color(0xFFCBEF43)
                    )
                }
            }
        }
    }
}

//@Composable
//fun PlanTripView(navController: NavController) {
//    Text(text = "This is PlanTripView")
//}
