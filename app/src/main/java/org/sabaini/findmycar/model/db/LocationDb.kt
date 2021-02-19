package org.sabaini.findmycar.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/* Provides a instance of the database */

@Database(entities = [DatabaseLocation::class], version = 2)
abstract class LocationDb : RoomDatabase() {
    abstract val  locationDao: LocationDao
}

private lateinit var INSTANCE: LocationDb

fun getDatabase(context: Context): LocationDb {
    synchronized(LocationDb::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LocationDb::class.java,
                "locations")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}