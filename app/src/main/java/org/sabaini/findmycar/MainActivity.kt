package org.sabaini.findmycar

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.sabaini.findmycar.databinding.ActivityMainBinding
import org.sabaini.findmycar.mvp.FindMyCarContract
import org.sabaini.findmycar.mvp.FindMyCarPresenter

private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

class MainActivity : AppCompatActivity(), FindMyCarContract.View {

    override lateinit var presenter: FindMyCarPresenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind the presenter to this activity and start it
        presenter = FindMyCarPresenter(this)
        presenter.start()

        // Get the current location of the device and set the position of the map.
        binding.btSaveLocation.setOnClickListener {
            presenter.getDeviceLocation()
        }

        // Get the saved location and set the position on the map.
        binding.btShowLocation.setOnClickListener {
            presenter.showLocation()
        }
    }

    /*
     * Prompts the user for permission to use the device location.
     */
    override fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            presenter.setPermission(true)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    /*
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        presenter.setPermission(false)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    presenter.setPermission(true)
                }
            }
        }
    }

    /*
    * Shows the address.
    */
    override fun showLocationText(text: String) {
        binding.txtCurrentLocation.text = text
    }
}