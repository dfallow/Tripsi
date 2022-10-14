package com.example.tripsi.data

import androidx.room.Embedded
import androidx.room.Relation

class TripWithStats {
    @Embedded
    var trip: Trip? = null

    @Relation(parentColumn = "tripId", entityColumn = "trip")
    var statistics: Statistics? = null
}