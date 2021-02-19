package org.sabaini.findmycar.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Class that represent the database table to store the locations*/
@Entity
data class DatabaseLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val latitude: Double,
    val longitude: Double
)