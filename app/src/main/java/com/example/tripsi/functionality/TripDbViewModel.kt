package com.example.tripsi.functionality

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripsi.data.*
import kotlinx.coroutines.launch

class TripDbViewModel(application: Application) : AndroidViewModel(application) {
    private val db = TripDatabase.get(application)

    //Trip
    fun getAllTrips(): LiveData<List<Trip>> = db.tripDao().getAll()

    fun getTripsByStatus(status: Int): LiveData<List<Trip>> = db.tripDao().getTripsByStatus(status)

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            db.tripDao().insert(trip)
        }
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

    //Note
    fun getTripNotes(tripId: Int): LiveData<List<Note>> = db.noteDao().getTripNotes(tripId)

    fun addNote(note: Note) {
        viewModelScope.launch {
            db.noteDao().insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            db.noteDao().update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            db.noteDao().delete(note)
        }
    }

    //Location
    fun getTripLocationData(tripId: Int): LiveData<List<Location>> =
        db.locationDao().getTripLocationData(tripId)

    fun getTripStartCoords(tripId: Int): LiveData<Location> =
        db.locationDao().getTripStartLocation(tripId)

    fun getTripEndCoords(tripId: Int): LiveData<Location> =
        db.locationDao().getTripEndLocation(tripId)

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

}