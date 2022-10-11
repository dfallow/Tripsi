package com.example.tripsi.screens.home

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tripsi.utils.Screen
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun ContactDetails(navController: NavController, application: Application) {
    val filename = "my_contact_information.txt"

    var name: String? by remember { mutableStateOf("") }
    var email: String? by remember { mutableStateOf("") }
    var phone: String? by remember { mutableStateOf("") }
    var website: String? by remember { mutableStateOf("") }
    var instagram: String? by remember { mutableStateOf("") }
    var tiktok: String? by remember { mutableStateOf("") }
    var linkedin: String? by remember { mutableStateOf("") }
    var github: String? by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 15.dp)
    ) {
        Text("Your contact information", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = name ?: "",
            onValueChange = { name = it },
            label = { Text("Your name") }
        )
        OutlinedTextField(
            value = email ?: "",
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = phone ?: "",
            onValueChange = { phone = it },
            label = { Text("Phone number") }
        )
        OutlinedTextField(
            value = website ?: "",
            onValueChange = { website = it },
            label = { Text("Website") }
        )
        OutlinedTextField(
            value = instagram ?: "",
            onValueChange = { instagram = it },
            label = { Text("Instagram") }
        )
        OutlinedTextField(
            value = tiktok ?: "",
            onValueChange = { tiktok = it },
            label = { Text("Tiktok") }
        )
        OutlinedTextField(
            value = linkedin ?: "",
            onValueChange = { linkedin = it },
            label = { Text("LinkedIn") }
        )
        OutlinedTextField(
            value = github ?: "",
            onValueChange = { github = it },
            label = { Text("Github") }
        )
        Button(onClick = {

            application.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write("Name: $name\n".toByteArray())
                it.write("Email: $email\n".toByteArray())
                it.write("Phone: $phone\n".toByteArray())
                it.write("Website: $website\n".toByteArray())
                it.write("Instagram: $instagram\n".toByteArray())
                it.write("Tiktok: $tiktok\n".toByteArray())
                it.write("LinkedIn: $linkedin\n".toByteArray())
                it.write("Github: $github\n".toByteArray())
            }


            //navController.navigate(Screen.HomeScreen.route)
        }, modifier = Modifier.padding(bottom = 40.dp)) {
            Text("Save")
        }
    }
}