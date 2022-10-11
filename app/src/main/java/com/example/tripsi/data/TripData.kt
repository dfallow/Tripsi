package com.example.tripsi.data

import androidx.room.Embedded
import androidx.room.Relation


class TripData {
    @Embedded
    var trip: Trip? = null

    @Relation(parentColumn = "tripId", entityColumn = "trip")
    var stats: Statistics? = null

    @Relation(parentColumn = "tripId", entityColumn = "trip")
    var image: List<Image>? = null

    @Relation(parentColumn = "tripId", entityColumn = "trip")
    var location: List<Location>? = null
}