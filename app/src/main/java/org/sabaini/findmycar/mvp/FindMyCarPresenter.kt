package org.sabaini.findmycar.mvp

import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.snackbar.Snackbar
import org.sabaini.findmycar.MainActivity
import org.sabaini.findmycar.R
import java.util.*

private val TAG = MainActivity::class.java.simpleName
private const val DEFAULT_ZOOM = 15F

class FindMyCarPresenter(private val view: FindMyCarContract.View) : FindMyCarContract.Presenter {

    // Map fragment and initialized map.
    private lateinit var mapFragment: SupportMapFragment
    private var map: GoogleMap? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Brazil) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-18.0517836, -53.47937)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    override fun start() {
        // Construct a PlacesClient.
        Places.initialize(
            (view as MainActivity).applicationContext, R.string.maps_api_key.toString()
        )
        placesClient = Places.createClient(view)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(view)

        // Build the map.
        mapFragment =
            view.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it

            // Move camera to the default location
            moveCamera(defaultLocation, 5F)

            // Prompt the user for permission.
            view.getLocationPermission()

            // Turn on the My Location layer and the related control on the map.
            updateLocationUI()
        }
    }

    // Return the LatLng object for the last known location
    private fun getLastKnownLocation(): LatLng {
        return LatLng(
            lastKnownLocation!!.latitude,
            lastKnownLocation!!.longitude
        )
    }

    // Places the camera on the map
    private fun moveCamera(location: LatLng, zoom: Float) {
        map?.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(location, zoom)
        )
    }

    // Places a marker in a location
    private fun addMarker(location: LatLng) {
        map?.addMarker(
            MarkerOptions()
                .title("You parked here")
                .position(location)
        )
    }

    // Get the address string of a location
    private fun getAddress(location: LatLng): String {
        val geocoder = Geocoder(view as MainActivity, Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )
        return addresses[0].getAddressLine(0)
    }

    /*
     * Gets the current location of the device, and positions the map's camera.
     */
    override fun saveLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(view as MainActivity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            addMarker(getLastKnownLocation())

                            moveCamera(getLastKnownLocation(), DEFAULT_ZOOM)

                            view.showLocationText(getAddress(getLastKnownLocation()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        moveCamera(defaultLocation, DEFAULT_ZOOM)
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun showLocation() {
        if (lastKnownLocation != null) {

            addMarker(getLastKnownLocation())

            moveCamera(getLastKnownLocation(), DEFAULT_ZOOM)

            view.showLocationText(getAddress(getLastKnownLocation()))
        } else {
            view.showSnackBar("Location not found!")
        }
    }

    override fun traceLocation() {
        TODO("Not yet implemented")
    }

    /*
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                view.getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    fun setPermission(value: Boolean) {
        locationPermissionGranted = value
    }
}