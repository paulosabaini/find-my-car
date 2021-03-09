package org.sabaini.findmycar.presenter

import org.sabaini.findmycar.model.api.Directions
import org.sabaini.findmycar.model.db.DatabaseLocation
import org.sabaini.findmycar.model.db.LocationDb

interface FindMyCarContract {
    interface Model : BaseModel<LocationDb> {
        suspend fun getLastLocation(): DatabaseLocation?
        suspend fun insertLocation(location: DatabaseLocation)
        suspend fun getDirections(origin: String, dest: String, key: String): Directions
    }

    interface View : BaseView<FindMyCarPresenter> {
        fun getLocationPermission()
        fun showLocationText(text: String)
        fun showSnackBar(text: String)
    }

    interface Presenter : BasePresenter {
        fun saveLocation()
        fun showLocation()
        fun routeLocation()
    }
}