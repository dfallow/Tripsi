package com.example.tripsi

import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tripsi.data.Image
import com.example.tripsi.data.Location
import com.example.tripsi.data.Note
import com.example.tripsi.data.Statistics
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.media.MediaView
import com.example.tripsi.ui.theme.TripsiTheme

class MainActivity : ComponentActivity() {
    private lateinit var model: TripDbViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = TripDbViewModel(application)
        //addData(model)

        setContent {
            TripsiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    MediaView(model)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TripsiTheme {
        Greeting("Android")
    }
}

fun addData(model: TripDbViewModel) {
    model.addImage(Image(0, "testfile100.jpg", 1))
    model.addNote(Note(0, "Don't wanna go home.", 1))
    model.addLocation(Location(0, 68.12, 35.19, "10-11-2022", 8, 3, 1))
}