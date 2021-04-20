package org.sabaini.findmycar.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.findmycar.model.api.Directions
import org.sabaini.findmycar.model.db.DatabaseLocation
import org.sabaini.findmycar.contract.FindMyCarContract
import org.sabaini.findmycar.model.api.DirectionsApi
import org.sabaini.findmycar.model.db.LocationDao
import javax.inject.Inject

/* Repository that provides data to the Presenter */

class Model @Inject constructor(
    override val locationDao: LocationDao,
    override val directionsApi: DirectionsApi
) : FindMyCarContract.Model {

    override suspend fun getLastLocation(): DatabaseLocation? {
        var lastLocation: DatabaseLocation? = null
        withContext(Dispatchers.IO) {
            try {
                lastLocation = locationDao.getLastLocation()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return lastLocation
    }

    override suspend fun insertLocation(latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            try {
                locationDao.insert(DatabaseLocation(null, latitude, longitude))
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
                directions = directionsApi.getDirections(origin, dest, key)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return directions!!
    }
}