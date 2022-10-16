package com.example.tripsi.functionality

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripsi.data.*
import com.example.tripsi.screens.currentTrip.CurrentTripViewModel
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class TripDbViewModel(application: Application) : AndroidViewModel(application) {
    private val db = TripDatabase.get(application)

    //this is used to pass tripId between Travel History View and Media View
    var tripId = 0

    lateinit var tripData: TripData
    lateinit var pastTripData: TripData

    var currentTripMoments = ArrayList<GeoPoint>()

    var currentTripMomentsNew = ArrayList<CurrentTripViewModel.Moment>()

    fun getCurrentTripMomentsNew(locations: List<Location>) {
        Log.d("momentLocations", "$locations")
        if (locations.isEmpty()) {
            currentTripMomentsNew = ArrayList()
        } else {
            for (location in locations) {
                currentTripMomentsNew += CurrentTripViewModel.Moment(
                    location.locationId,
                    GeoPoint(location.coordsLatitude, location.coordsLongitude),
                    description = "need to get",
                    photos = null,
                    position = location.position.position,
                    CurrentTripViewModel.MomentInfo("", "", "")
                )
            }
        }
        Log.d("momentLocations", "$currentTripMomentsNew")
    }

    fun getTripMoments(locations: List<Location>) {
        if (currentTripMoments.isEmpty()) {
            Log.d("CurrentTripMomentHello", locations.toString())
            if (locations.isEmpty()) {
                currentTripMoments = ArrayList()
                Log.d("CurrentTripMoment4", currentTripMoments.toString())
            } else {
                for (location in locations) {
                    currentTripMoments += GeoPoint(
                        location.coordsLatitude,
                        location.coordsLongitude
                    )
                    Log.d("CurrentTripMoment3", currentTripMoments.toString())
                }
            }
        }
    }


    //Trip

    fun getAllTrips(): LiveData<List<Trip>> = db.tripDao().getAll()

    fun addTrip(trip: Trip): Int? {
        var tripId: Int? = null
        viewModelScope.launch {
            tripId = (db.tripDao().insert(trip)).toInt()
        }
        return tripId
    }

    fun deleteTripById(tripId: Int) {
        viewModelScope.launch {
            db.tripDao().deleteTripById(tripId)
        }
    }

    fun updateTripStatus(status: Int, tripId: Int) {
        viewModelScope.launch {
            db.tripDao().updateTripStatus(status, tripId)
        }
    }

    fun updateTripTravelMethod(travelMethod: Int, tripId: Int) {
        viewModelScope.launch {
            db.tripDao().updateTripTravelMethod(travelMethod, tripId)
        }
    }


    //Statistics

    fun addTripStats(stats: Statistics) {
        viewModelScope.launch {
            db.statisticsDao().insert(stats)
        }
    }


    //Image

    fun addImage(image: Image) {
        viewModelScope.launch {
            db.imageDao().insert(image)
        }
    }

    //Location

    fun getLocationWithMedia(tripId: Int): LiveData<List<LocationWithImagesAndNotes>> =
        db.locationDao().getLocationsWithMedia(tripId)

    fun getMomentWithMedia(momentId: String): LiveData<LocationWithImagesAndNotes> =
        db.locationDao().getAllLocationImagesAndNote(momentId)

    fun addLocation(location: Location) {
        viewModelScope.launch {
            db.locationDao().insert(location)
        }
    }


    //TripData

    // get one trip and all of its related data
    fun getTripData(tripId: Int): LiveData<TripData> = db.tripDao().getTripData(tripId)

    //get all trips and their data by trip status
    fun getAllTripsDataByStatus(status: Int): LiveData<List<TripData>> =
        db.tripDao().getAllTripDataByStatus(status)
}