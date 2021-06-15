package com.adrcotfas.locationexplorer.business

import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Encapsulates all the location related logic.
 */
class LocationProvider @Inject constructor(@ApplicationContext context: Context, var listener: Listener) {

    interface Listener {
        fun onLocationResult(lat: Double, lon: Double)
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            listener.onLocationResult(
                locationResult.lastLocation.latitude,
                locationResult.lastLocation.longitude
            )
        }
    }

    /**
     * Start listening for location updates. When a new location is received, notify the listener.
     */
    fun start() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create().apply {
                    //TODO: use longer durations to save battery
                    interval = TimeUnit.SECONDS.toMillis(10)
                    fastestInterval = TimeUnit.SECONDS.toMillis(5)
                    maxWaitTime =
                        TimeUnit.SECONDS.toMillis(10)
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    smallestDisplacement = 100f // [m]
                },
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permissions. $unlikely")
        }
    }

    /**
     * Stop listening for location updates.
     */
    fun stop() {
        try {
            val removeTask =
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")

                } else {
                    Log.d(TAG, "Failed to remove Location Callback.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permissions. $unlikely")
        }
    }

    companion object {
        private const val TAG = "LocationProvider"
    }
}