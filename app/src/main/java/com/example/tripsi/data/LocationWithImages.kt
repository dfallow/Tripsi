package com.example.tripsi.data

import androidx.room.Embedded
import androidx.room.Relation

class LocationWithImagesAndNotes {
    @Embedded
    var location: Location? = null

    @Relation(parentColumn = "locationId", entityColumn = "location")
    var locationImages: List<Image>? = null
}