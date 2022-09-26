package com.example.tripsi.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val distance: Double = 0.0,
    val duration: Double = 0.0,
    val speed: Double = 0.0
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
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imgId: Int,
    val filename: String?,
    val trip: Int,
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
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int,
    val noteText: String?,
    val trip: Int,
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["tripId"],
        childColumns = ["trip"]
    ),
        ForeignKey(
            entity = Image::class,
            onDelete = CASCADE,
            parentColumns = ["imgId"],
            childColumns = ["image"]
        ),
        ForeignKey(
            entity = Note::class,
            onDelete = CASCADE,
            parentColumns = ["noteId"],
            childColumns = ["note"]
        )
    ],
    indices = [
        Index("trip"),
        Index("image"),
        Index("note"),
    ]
)
data class Location(
    @PrimaryKey(autoGenerate = true)
    val locationId: Int,
    val coordsLatitude: Double,
    val coordsLongitude: Double,
    // TODO: change date data type
    val date: String,
    val image: Int?,
    val note: Int?,
    val trip: Int,
    val isStart: Boolean = false,
    val isEnd: Boolean = false
)
