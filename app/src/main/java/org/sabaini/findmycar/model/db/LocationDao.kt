package org.sabaini.findmycar.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/* Data access operations in the database */

@Dao
interface LocationDao {

    @Insert
    fun insert(location: DatabaseLocation)

    @Query("SELECT * FROM databaselocation ORDER BY id DESC LIMIT 1")
    fun getLastLocation(): DatabaseLocation
}