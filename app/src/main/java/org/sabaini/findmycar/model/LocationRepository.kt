package org.sabaini.findmycar.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.findmycar.model.db.DatabaseLocation
import org.sabaini.findmycar.model.db.LocationDb

/* Repository that provides data to the Presenter */

class LocationRepository(private val database: LocationDb) {

    suspend fun getLastLocation(): DatabaseLocation? {
        var lastLocation: DatabaseLocation? = null
        withContext(Dispatchers.IO) {
            try {
                lastLocation = database.locationDao.getLastLocation()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return lastLocation
    }

    suspend fun insertLocation(location: DatabaseLocation) {
        withContext(Dispatchers.IO) {
            try {
                database.locationDao.insert(location)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }
}