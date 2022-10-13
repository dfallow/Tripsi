@file:Suppress("NAME_SHADOWING")

package com.example.tripsi.screens.planTrip

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripsi.R
import com.example.tripsi.data.TravelMethod
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.LockScreenOrientation
import java.util.*



@Composable
fun PlanTripView(navController: NavController, tripDbViewModel: TripDbViewModel) {


    val planTripViewModel = PlanTripViewModel()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .background( color = MaterialTheme.colors.background),
        verticalArrangement = Arrangement.SpaceEvenly
        )
    {
        Text(
            text = "Create a plan for your upcoming trip",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onPrimary,
            )

        DatePicker(planTripViewModel)
        TextInput(planTripViewModel)
        //MapAddressPickerView(planTripViewModel)
        FinalLocationPicker(planTripViewModel)
        TripType(planTripViewModel)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,

        ) {
            Button(
                onClick = {
                    val saved = planTripViewModel.saveTrip(tripDbViewModel, context)
                    if (saved) navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary),            ) {
                Text(
                    text = "Save", fontWeight = FontWeight.ExtraBold
                )
            }
            Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSurface),
            ) {
                Text(
                    text = "Discard",
                    fontWeight = FontWeight.ExtraBold,
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
        label = { Text("Name of your trip", color = MaterialTheme.colors.onPrimary,) },
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onPrimary,
            unfocusedBorderColor = MaterialTheme.colors.onSurface,
            textColor = MaterialTheme.colors.onPrimary
        )
    )
}

@Composable
fun FinalLocationPicker(planTripViewModel: PlanTripViewModel) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val currentLocation = planTripViewModel.location.collectAsState()


    currentLocation.value.let {
        if (planTripViewModel.searchState.value == SearchState.SAVED.status) {
            text = planTripViewModel.getAddressFromLocation(context)
        }
    }
    OutlinedTextField(
        value = text,
        onValueChange = {
            planTripViewModel.tripDestination = it
            text = it
            if (planTripViewModel.searchState.value == SearchState.SEARCHING.status) {
                planTripViewModel.onTextChanged(context, text)
            }
        },
        trailingIcon = {
            when (planTripViewModel.searchState.value) {
                SearchState.EMPTY.status -> {
                }
                SearchState.SEARCHING.status -> {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "search text",
                        modifier = Modifier.clickable {
                            if (text != "") {
                                    focusManager.clearFocus(true)
                                    planTripViewModel.searchState.value = SearchState.SAVED.status
                            } else {
                                Toast.makeText(context, "Please Enter a Destination", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                SearchState.SAVED.status -> {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier.clickable {
                            text = ""
                            planTripViewModel.searchState.value = SearchState.SEARCHING.status
                            focusRequester.requestFocus()
                        }
                    )
                }
            }

        },
        label = { Text("What's your final destination", color = MaterialTheme.colors.onPrimary) },

        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                planTripViewModel.addressText.value = ""
                planTripViewModel.searchState.value = SearchState.SEARCHING.status
            },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (text != "") {
                    focusManager.clearFocus(true)
                    planTripViewModel.searchState.value = SearchState.SAVED.status
                } else {
                    Toast.makeText(context, "Please Enter a Destination", Toast.LENGTH_SHORT).show()
                }
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onPrimary,
            unfocusedBorderColor = MaterialTheme.colors.onSurface,
            textColor = MaterialTheme.colors.onPrimary

        )
    )
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
    mDatePickerDialog.datePicker.minDate = mCalendar.timeInMillis

    mDatePickerDialog.datePicker.minDate = mCalendar.timeInMillis

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background( MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(5.dp)),
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
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.secondaryVariant),
        )
        {
            Icon(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "Calendar",
               // tint = MaterialTheme.colors.secondaryVariant
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
            textAlign = TextAlign.Center,
            color= MaterialTheme.colors.onSurface

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

    Text(text = "Selected Type of trip: ${selectedValue.value.ifEmpty { "NONE" }}", color = MaterialTheme.colors.onPrimary)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.onBackground,shape = RoundedCornerShape(5.dp)),
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
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                    TravelMethod.BUS -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_bus else R.drawable.bus),
                        contentDescription = "bus",
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                    TravelMethod.BIKE -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_bike else R.drawable.bike),
                        contentDescription = "bike",
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                    TravelMethod.WALK -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_walk else R.drawable.walk),
                        contentDescription = "walk",
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                    TravelMethod.PLANE -> Icon(
                        painter = painterResource(if (isSelectedItem(item.name)) R.drawable.unselected_plane else R.drawable.plane),
                        contentDescription = "plane",
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
    }
}



/*

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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)) {
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
                            if (planTripViewModel.isMapEditable.value) {Icons.Default.Clear} else {Icons.Default.Search},
                            contentDescription = "clear or search text",
                            modifier = Modifier.clickable { text = ""
                                planTripViewModel.isMapEditable.value =
                                !planTripViewModel.isMapEditable.value })

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

        }

    }
}
*/