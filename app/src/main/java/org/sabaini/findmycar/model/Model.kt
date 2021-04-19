package org.sabaini.findmycar.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.findmycar.model.api.Directions
import org.sabaini.findmycar.model.api.Network
import org.sabaini.findmycar.model.db.DatabaseLocation
import org.sabaini.findmycar.model.db.LocationDb
import org.sabaini.findmycar.contract.FindMyCarContract

/* Repository that provides data to the Presenter */

class Model(override val database: LocationDb) : FindMyCarContract.Model {

    override suspend fun getLastLocation(): DatabaseLocation? {
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

    override suspend fun insertLocation(location: DatabaseLocation) {
        withContext(Dispatchers.IO) {
            try {
                database.locationDao.insert(location)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    /*
     * Make a request to the Directions API and return a Directions object.
     */
    override suspend fun getDirections(origin: String, dest: String, key: String): Directions {
        var directions: Directions? = null
        withContext(Dispatchers.IO) {
            try {
                directions = Network.retrofitService.getDirections(origin, dest, key)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return directions!!
    }
}