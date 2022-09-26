package com.example.tripsi.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        (Trip::class),
        (Statistics::class),
        (Image::class),
        (Note::class),
        (Coordinates::class)],
    version = 1
)
abstract class Database: RoomDatabase() {
}