package com.example.tripsi.screens.currentTrip

import android.Manifest
import android.app.Activity
import android.util.Log
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsi.data.Image
import com.example.tripsi.data.Statistics
import com.example.tripsi.data.TripStatus
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import java.util.*

class CurrentTripViewModel : ViewModel() {

    // Data classes for temporary information for moments which update the UI without data stored in database
    data class Moment(
        val id: String,
        val location: GeoPoint,
        val description: String?,
        val photos: MutableList<Bitmap?>?,
        val position: Int,
        val info: MomentInfo
    )

    data class MomentInfo(val date: String, val time: String, val location: String)

    // Temporary status of trip, used to update UI
    var currentStatus by mutableStateOf(TripStatus.UPCOMING.status)
    fun startActive() {
        currentStatus = TripStatus.ACTIVE.status
    }

    fun endActive() {
        currentStatus = TripStatus.PAST.status
    }

    // Displays popupMoment when user clicks on icon
    var showMoment by mutableStateOf(false)
    fun displayMoment() {
        showMoment = true
    }

    fun hideMoment() {
        showMoment = false
    }

    // Used to update UI when a moment is added
    // TODO find a better way
    var showText by mutableStateOf(false)
    fun toggleText() {
        showText = !showText
    }


    // Array of GeoPoints for ViewModel
    val currentTripMomentsNew: MutableLiveData<ArrayList<Moment>> by lazy {
        MutableLiveData<ArrayList<Moment>>()
    }

    // value is just used to update the MutableLiveData object
    private var tempMomentArrayNew = ArrayList<Moment>()

    fun addStartLocationNew(location: Location) {
        val momentId = UUID.randomUUID().toString()
        tempMomentArrayNew.clear()
        tempMomentArrayNew += Moment(
            momentId,
            GeoPoint(location.userLocation.latitude, location.userLocation.longitude),
            null,
            null,
            MomentPosition.START.position,
            MomentInfo("", "", "")
        )
        currentTripMomentsNew.value = tempMomentArrayNew
    }

    fun addLocationNew(
        location: com.example.tripsi.data.Location,
        description: String?,
        photos: MutableList<Bitmap?>?,
        info: MomentInfo
    ) {
        tempMomentArrayNew += Moment(
            viewModel.momentId.value,
            GeoPoint(location.coordsLatitude, location.coordsLongitude),
            description,
            photos,
            MomentPosition.MIDDLE.position,
            info
        )
        currentTripMomentsNew.value = tempMomentArrayNew
    }

    fun addEndLocationNew(location: Location) {
        val momentId = UUID.randomUUID().toString()
        tempMomentArrayNew += Moment(
            momentId,
            GeoPoint(location.userLocation.latitude, location.userLocation.longitude),
            null,
            null,
            MomentPosition.END.position,
            MomentInfo("", "", "")
        )
        currentTripMomentsNew.value = tempMomentArrayNew
    }

    // Moment variables
    // momentId will get randomly generated after first use
    val momentId = mutableStateOf(UUID.randomUUID().toString())
    val fromDatabase = mutableStateOf(false)
    var temporaryPhotos = mutableListOf<Bitmap?>()
    val mapMoments = mutableListOf<Int>()
    val currentIndex = mutableStateOf(0)
    lateinit var momentInfo: MomentInfo
    lateinit var currentLocation: GeoPoint
    lateinit var currentMomentId: String

    // clears temporary value and resets the currentStatus, also removes location updates
    fun goHome(location: Location) {
        tempMomentArrayNew.clear()
        currentTripMomentsNew.value = tempMomentArrayNew

        // This won't affect UI in future as we will navigate to another screen
        currentStatus = TripStatus.UPCOMING.status

        location.stopUpdatingLocation()
    }

    // Currently storing generic properties for buttons
    val modifier = Modifier
        .width(130.dp)
    val shape = RoundedCornerShape(10.dp)


    //Information needed for storing data to database
    var momentLocation: com.example.tripsi.data.Location? = null //current location data
    private var locationId = UUID.randomUUID()
        .toString() //unique id for location that will be used to store the entry in db
    var momentNote: MutableLiveData<String?> =
        MutableLiveData(null) //note/comment that the user enters
    var momentImageFilenames: MutableList<String> =
        mutableListOf() //list of filenames of all images that were taken in a moment

    suspend fun saveImageToDb(tripDbViewModel: TripDbViewModel, filename: String) {
        //get current tripId
        val trip = tripDbViewModel.tripData.trip!!.tripId

        if (momentLocation != null)
            withContext(Dispatchers.IO) {
                try {
                    //save image data to database
                    tripDbViewModel.addImage(Image(0, filename, momentNote.value, trip, locationId))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

    }

    fun saveLocationToDb(tripDbViewModel: TripDbViewModel, context: Context) {
        //get current tripId
        val trip = tripDbViewModel.tripData.trip!!.tripId

        viewModelScope.launch(Dispatchers.IO) {
            if (momentLocation != null) {
                try {
                    //save location data to database
                    tripDbViewModel.addLocation(
                        com.example.tripsi.data.Location(
                            locationId,
                            momentLocation!!.coordsLatitude,
                            momentLocation!!.coordsLongitude,
                            momentLocation!!.date,
                            trip,
                            position = MomentPosition.MIDDLE,
                            hasMedia = true //this indicates that the location has images associated with it
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    context,
                    "Something went wrong. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun clearData() {
        momentLocation = null
        momentNote.value = null
        momentImageFilenames.clear()
        locationId = UUID.randomUUID().toString()
    }

    // Variables and fun for counting steps
    var currentSteps = MutableLiveData(0)
    private val stepsBottomLine = MutableLiveData<Int>(null)
    private var distance = MutableLiveData(0)


    // Setting step counting
    fun setSteps(stepAmount: Int) {
        var steps = stepAmount
        if (stepsBottomLine.value == null) {
            stepsBottomLine.value = stepAmount
            steps++
        }
        currentSteps.value = steps - (stepsBottomLine.value ?: 0)
    }

    fun resetSteps() {
        currentSteps.value?.let {
            stepsBottomLine.value = (stepsBottomLine.value ?: 0).plus(it)
        }
        setSteps(stepsBottomLine.value ?: 0)
    }

    var stepsForDb : MutableLiveData<Int> = MutableLiveData(0)

    // Calculating distance based on steps
    // 74cm is average step distance
    fun setDistance(stepAmount: Int): Int {
        distance.value = (stepAmount * 70) / 100
        stepsForDb.postValue(stepAmount)
        return distance.value!!
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkPermission(context: Context) =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED

    fun requestPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                0
            )
        }
    }

    fun saveStatisticsToDb(tripDbViewModel: TripDbViewModel) {
        //get current tripId
        val trip = tripDbViewModel.tripData.trip!!.tripId

        try {
            viewModelScope.launch(Dispatchers.IO) {
                tripDbViewModel.addTripStats(Statistics(0, trip, distance.value ?: 0, stepsForDb.value ?: 0))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}