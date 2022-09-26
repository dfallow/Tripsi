package com.example.tripsi.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val destination: String,
    val travelMethod: Int,
    val status: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["id"],
        childColumns = ["trip"]
    )]
)
data class Statistics(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trip: Int,
    val distance: Double,
    val duration: Double,
    val speed: Double
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["id"],
        childColumns = ["trip"]
    )]
)
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val filename: String,
    val trip: Int,
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["id"],
        childColumns = ["trip"]
    )]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val noteText: String?,
    val trip: Int,
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        onDelete = CASCADE,
        parentColumns = ["id"],
        childColumns = ["trip"]
    ),
        ForeignKey(
            entity = Image::class,
            onDelete = CASCADE,
            parentColumns = ["id"],
            childColumns = ["image"]
        ),
        ForeignKey(
            entity = Note::class,
            onDelete = CASCADE,
            parentColumns = ["id"],
            childColumns = ["note"]
        )
    ]
)
data class Coordinates(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val coordsLatitude: Double,
    val coordsLongitude: Double,
    // TODO: might need to change date type
    val date: LocalDateTime,
    val image: Int?,
    val note: Int?,
    val trip: Int,
    val isStart: Boolean,
    val isEnd: Boolean
)


// TODO: move both enum classes somewhere else
enum class TravelMethod(val method: Int) {
    CAR(1),
    BIKE(2),
    WALK(3),
    FLY(4),
    OTHER(5)
}

enum class Status(val status: Int) {
    UPCOMING(1),
    ACTIVE(2),
    PAST(3)
}
