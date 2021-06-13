package com.adrcotfas.locationexplorer

import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationProvider(context: Context, private val listener: Listener) {

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

    fun start() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create().apply {
                    //TODO: adapt the values
                    interval = TimeUnit.SECONDS.toMillis(1) //60
                    fastestInterval = TimeUnit.SECONDS.toMillis(1) //30
                    maxWaitTime =
                        TimeUnit.SECONDS.toMillis(1) // maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    //smallestDisplacement = 10f // meters
                },
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permissions. $unlikely")
        }
    }

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