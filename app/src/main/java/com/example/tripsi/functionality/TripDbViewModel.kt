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
        Log.d("momentLocations","$currentTripMomentsNew")
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

    fun getTripsByStatus(status: Int): LiveData<List<Trip>> = db.tripDao().getTripsByStatus(status)

    fun addTrip(trip: Trip): Int? {
        var tripId: Int? = null
        viewModelScope.launch {
            tripId = (db.tripDao().insert(trip)).toInt()
        }
        return tripId
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            db.tripDao().delete(trip)
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

    fun getTripStats(tripId: Int): LiveData<Statistics> = db.statisticsDao().getTripStats(tripId)

    fun addTripStats(stats: Statistics) {
        viewModelScope.launch {
            db.statisticsDao().insert(stats)
        }
    }

    fun updateTripStats(statistics: Statistics) {
        viewModelScope.launch {
            db.statisticsDao().update(statistics)
        }
    }


    //Image

    fun getTripImages(tripId: Int): LiveData<List<Image>> = db.imageDao().getTripImages(tripId)

    fun getImageById(imageId: Int): LiveData<Image> = db.imageDao().getImageById(imageId)

    fun getImageByFilename(filename: String): LiveData<Image> =
        db.imageDao().getImageByFilename(filename)

    fun addImage(image: Image) {
        viewModelScope.launch {
            db.imageDao().insert(image)
        }
    }

    fun deleteImage(image: Image) {
        viewModelScope.launch {
            db.imageDao().delete(image)
        }
    }


    //Location

    fun getTripLocationData(tripId: Int): LiveData<List<LocationWithImagesAndNotes>> =
        db.locationDao().getTripLocationData(tripId)

    fun getTripStartCoords(tripId: Int): LiveData<LocationWithImagesAndNotes> =
        db.locationDao().getTripStartLocation(tripId)

    fun getTripEndCoords(tripId: Int): LiveData<LocationWithImagesAndNotes> =
        db.locationDao().getTripEndLocation(tripId)

    fun getLocationWithMedia(tripId: Int): LiveData<List<LocationWithImagesAndNotes>> =
        db.locationDao().getLocationsWithMedia(tripId)

    fun getMomentWithMedia(momentId: String): LiveData<LocationWithImagesAndNotes> =
        db.locationDao().getAllLocationImagesAndNote(momentId)

    fun addLocation(location: Location) {
        viewModelScope.launch {
            db.locationDao().insert(location)
        }
    }

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            db.locationDao().update(location)
        }
    }


    //TripData

    // get one trip and all of its related data
    fun getTripData(tripId: Int): LiveData<TripData> = db.tripDao().getTripData(tripId)

    //get all trips and their related data
    fun getAllTripsData(): LiveData<List<TripData>> = db.tripDao().getAllTripsData()

    //get all trips and their data by trip status
    fun getAllTripsDataByStatus(status: Int): LiveData<List<TripData>> =
        db.tripDao().getAllTripDataByStatus(status)


    //LocationWithImagesAndNotes

    //get location with all its images
    fun getAllLocationImagesAndNote(locationId: String): LiveData<LocationWithImagesAndNotes> =
        db.locationDao().getAllLocationImagesAndNote(locationId)

}