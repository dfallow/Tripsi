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

    //delete with Id
    @Query("DELETE FROM trip WHERE trip.tripId = :tripId")
    suspend fun deleteTripById(tripId: Int)

    //get all trips
    @Query("SELECT * FROM trip")
    fun getAll(): LiveData<List<Trip>>

    //get tripWithStats
    @Transaction
    @Query("SELECT * FROM trip WHERE trip.tripId = :tripId")
    fun getTripStats(tripId: Int): LiveData<TripWithStats>

    //get trips based on status
    @Query("SELECT * FROM trip WHERE trip.status = :status")
    fun getTripsByStatus(status: Int): LiveData<List<Trip>>

    //update trip status
    @Query("UPDATE trip SET status = :status WHERE trip.tripId = :tripId")
    suspend fun updateTripStatus(status: Int, tripId: Int)

    //update trip travel method
    @Query("UPDATE trip SET travelMethod = :travelMethod WHERE trip.tripId = :tripId")
    suspend fun updateTripTravelMethod(travelMethod: Int, tripId: Int)

    @Transaction
    @Query("SELECT * FROM trip WHERE trip.tripId = :tripId")
    fun getTripData(tripId: Int): LiveData<TripData>

    @Transaction
    @Query("SELECT * FROM trip")
    fun getAllTripsData(): LiveData<List<TripData>>

    @Transaction
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

    //get image by filename
    @Query("SELECT * FROM image WHERE image.filename = :filename")
    fun getImageByFilename(filename: String): LiveData<Image>


}

@Dao
interface LocationDao : BaseDao<Location> {

    //get all coordinates from the trip
    @Query("SELECT * FROM location WHERE location.trip = :tripId")
    fun getTripLocationData(tripId: Int): LiveData<List<LocationWithImagesAndNotes>>

    //get start coordinates of trip
    @Query("SELECT * FROM location WHERE location.isStart = 'true' AND location.trip = :tripId")
    fun getTripStartLocation(tripId: Int): LiveData<LocationWithImagesAndNotes>

    //get end coordinates of the trip
    @Query("SELECT * FROM location WHERE location.isEnd = 'true' AND location.trip = :tripId")
    fun getTripEndLocation(tripId: Int): LiveData<LocationWithImagesAndNotes>

    //get only those locations of the trip that have media
    @Query("SELECT * FROM location WHERE location.trip = :tripId AND location.hasMedia = :hasMedia")
    fun getLocationsWithMedia(tripId: Int, hasMedia: Boolean = true): LiveData<List<LocationWithImagesAndNotes>>

    //get all images and a note for a particular location
    @Transaction
    @Query("SELECT * FROM location WHERE location.locationId = :locationId")
    fun getAllLocationImagesAndNote(locationId: String): LiveData<LocationWithImagesAndNotes>
}