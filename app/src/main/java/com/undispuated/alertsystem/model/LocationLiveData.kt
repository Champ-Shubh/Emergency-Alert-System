package com.undispuated.alertsystem.model

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.undispuated.alertsystem.R
import kotlinx.android.synthetic.main.fragment_medical.view.*
import java.util.*

class LocationLiveData(val context: Context): LiveData<Location>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    //onInactive is called when the lifecycle owner is paused, stopped or destroyed
    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // onActive is called when the lifecycle owner is started or resumed
    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        //Before beginning to start continuous listening to live location, we will get last known location
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                it?.also {
                    value = it
                }
            }
        startLocationUpdates()
    }

    // locationRequest is used to request location from the FusedLocationProvider Client
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 100000   //Listen to live location after every 100 seconds
            fastestInterval = 50000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    // locationCallback is invoked when the FusedLocationProvider Client returns results
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                value = location
            }
        }
    }

    //This function is for continuously listening to location updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }
}