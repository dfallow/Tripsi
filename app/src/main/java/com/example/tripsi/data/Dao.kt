package com.example.tripsi.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T): Long

    @Delete
    suspend fun delete(item: T)

    @Update
    suspend fun update(item: T)
}

@Dao
interface TripDao : BaseDao<Trip> {

    //get all trips
    @Query("SELECT * FROM trip")
    fun getAll(): LiveData<List<Trip>>

    //get trips based on status
    @Query("SELECT * FROM trip WHERE trip.status = :status")
    fun getTripsByStatus(status: Int): LiveData<List<Trip>>

    //update trip status
    @Query("UPDATE trip SET status = :status WHERE trip.tripId = :tripId")
    suspend fun updateTripStatus(status: Int, tripId: Int)

    @Query("SELECT * FROM trip WHERE trip.tripId = :tripId")
    fun getTripData(tripId: Int): LiveData<TripData>

    @Query("SELECT * FROM trip")
    fun getAllTripsData(): LiveData<List<TripData>>

    @Query("SELECT * FROM trip WHERE trip.status = :status")
    fun getAllTripDataByStatus(status: Int): LiveData<List<TripData>>
}

@Dao
interface StatisticsDao : BaseDao<Statistics> {

    //get trip statistics
    @Query("SELECT * FROM statistics WHERE statistics.trip = :tripId")
    fun getTripStats(tripId: Int): LiveData<Statistics>
}

@Dao
interface ImageDao : BaseDao<Image> {

    //get all images from a trip
    @Query("SELECT * FROM image WHERE image.trip = :tripId")
    fun getTripImages(tripId: Int): LiveData<List<Image>>

    //get image by id
    @Query("SELECT * FROM image WHERE image.imgId = :imageId")
    fun getImageById(imageId: Int): LiveData<Image>


}

@Dao
interface NoteDao : BaseDao<Note> {

    //get all notes from a trip
    @Query("SELECT * FROM note WHERE note.trip = :tripId")
    fun getTripNotes(tripId: Int): LiveData<List<Note>>

    //get one note by id
    @Query("SELECT * FROM note WHERE note.noteId = :noteId")
    fun getNoteById(noteId: Int): LiveData<Note>
}

@Dao
interface LocationDao : BaseDao<Location> {

    //get all coordinates from the trip
    @Query("SELECT * FROM location WHERE location.trip = :tripId")
    fun getTripLocationData(tripId: Int): LiveData<List<Location>>

    //get start coordinates of trip
    @Query("SELECT * FROM location WHERE location.isStart = 'true' AND location.trip = :tripId")
    fun getTripStartLocation(tripId: Int): LiveData<Location>

    //get end coordinates of the trip
    @Query("SELECT * FROM location WHERE location.isEnd = 'true' AND location.trip = :tripId")
    fun getTripEndLocation(tripId: Int): LiveData<Location>
}