package com.example.tripsi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        (Trip::class),
        (Statistics::class),
        (Image::class),
        (Note::class),
        (Location::class)],
    version = 1
)
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun statisticsDao(): StatisticsDao
    abstract fun imageDao(): ImageDao
    abstract fun noteDao(): NoteDao
    abstract fun locationDao(): LocationDao

    //ensure that there is only ONE instance of the database
    companion object {
        private var sInstance: TripDatabase? = null

        //synchronized ensures that only one thread at a time can call the get method
        @Synchronized
        fun get(context: Context): TripDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TripDatabase::class.java,
                    "trips.db"
                )
                    .build()
            }
            return sInstance!!
        }
    }
}