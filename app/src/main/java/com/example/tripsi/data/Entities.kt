package com.example.tripsi.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tripsi.screens.currentTrip.MomentPosition

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val tripId: Int,
    val tripName: String,
    val destination: String,
    val travelMethod: Int,
    val status: Int,
    // TODO:change date data type
    val plannedStartDate: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["tripId"],
        childColumns = ["trip"]
    )],
    indices = [Index("trip")]
)
data class Statistics(
    @PrimaryKey(autoGenerate = true)
    val statsId: Int,
    val trip: Int,
    val distance: Int,
    val steps: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["tripId"],
        childColumns = ["trip"]
    ),
        ForeignKey(entity = Location::class,
            onDelete = CASCADE,
            parentColumns = ["locationId"],
            childColumns = ["location"]
        )],
    indices = [Index("trip"), Index("location")]
)
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imgId: Int,
    val filename: String?,
    val comment: String?,
    val trip: Int,
    val location: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["tripId"],
        childColumns = ["trip"]
    ),
    ],
    indices = [
        Index("trip"),
    ]
)
data class Location(
    @PrimaryKey(autoGenerate = false)
    val locationId: String,
    val coordsLatitude: Double,
    val coordsLongitude: Double,
    // TODO: change date data type
    val date: String,
    val trip: Int,
    val position: MomentPosition,
    val isStart: Boolean = false,
    val isEnd: Boolean = false,
    val hasMedia: Boolean = false,
)
