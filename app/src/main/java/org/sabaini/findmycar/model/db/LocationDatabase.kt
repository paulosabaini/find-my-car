package org.sabaini.findmycar.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

/* Database to save the locations */

@Database(entities = [DatabaseLocation::class], exportSchema = false , version = 2)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}