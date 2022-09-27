package com.example.tripsi.screens.planTrip

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripsi.ui.theme.TripsiTheme
import java.util.*

@Composable
fun PlanTrip(){


    Column(modifier = Modifier
        .padding(10.dp)
        .fillMaxSize()

    ) {
        Text(text = "Create a plan for your\n" +
                "upcomming trip")
        DatePicker()
        Spacer(modifier = Modifier.padding(10.dp))
        TextInput()
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = "What is your final destination?")
        MapAddressPickerView(viewModel = PlanTripViewModel())
        TripType()
    }
}
@Composable
fun TextInput() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Name of your trip") }
    )
}

@Composable
fun MapAddressPickerView(viewModel: PlanTripViewModel){
    Surface(color = MaterialTheme.colors.background) {
        val currentLocation = viewModel.location.collectAsState()
        var text by remember { viewModel.addressText }
        val context = LocalContext.current

        Column(Modifier.fillMaxWidth()) {

            Box{
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        if(!viewModel.isMapEditable.value)
                            viewModel.onTextChanged(context, text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    enabled = !viewModel.isMapEditable.value,
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.End
                ){
                    Button(
                        onClick = {
                            viewModel.isMapEditable.value = !viewModel.isMapEditable.value
                        }
                    ) {
                        Text(text = if(viewModel.isMapEditable.value) "Edit" else "Save")
                    }
                }
            }

            Box(modifier = Modifier.height(10.dp)){

                currentLocation.value.let {
                    if(viewModel.isMapEditable.value) {
                        text = viewModel.getAddressFromLocation(context)
                    }
                }

            }
        }
    }
}

@Composable
fun DatePicker(){

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
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Column(modifier = Modifier,
        //verticalArrangement = Arrangement.Center,
       // horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Creating a button that on
        // click displays/shows the DatePickerDialog
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
            Text(text = "Date", color = Color.White)
        }

        // Adding a space of 100dp height
        Spacer(modifier = Modifier.size(10.dp))

        // Displaying the mDate value in the Text
        Text(text = "Selected Date: ${mDate.value}", fontSize = 10.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun TripType() {
    val selectedValue = remember { mutableStateOf("") }

    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
    val onChangeState: (String) -> Unit = { selectedValue.value = it }

    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    Column() {
        Text(text = "Selected Type of trip: ${selectedValue.value.ifEmpty { "NONE" }}")
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(item),
                        onClick = { onChangeState(item) },
                        role = Role.RadioButton
                    )
                    .padding(4.dp)
            ) {
                RadioButton(
                    selected = isSelectedItem(item),
                    onClick = null
                )
                Text(
                    text = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TripsiTheme {
        PlanTrip()
    }
}