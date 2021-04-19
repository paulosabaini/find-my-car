package org.sabaini.findmycar.utilities

import org.sabaini.findmycar.view.MainActivity

object Constants {
    const val BASE_URL = "https://maps.googleapis.com/maps/api/"
    const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    const val DEFAULT_ZOOM = 15F
    val TAG = MainActivity::class.java.simpleName
}