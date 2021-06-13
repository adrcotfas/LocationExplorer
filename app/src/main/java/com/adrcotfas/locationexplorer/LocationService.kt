package com.adrcotfas.locationexplorer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.adrcotfas.locationexplorer.NotificationHelper.Companion.EXPLORER_NOTIFICATION_ID

class LocationService : Service() {

    private lateinit var locationProvider: LocationProvider

    private val locationListener = object : LocationProvider.Listener {
        override fun onLocationResult(lat: Double, lon: Double) {
            Log.d(TAG, "New location: $lat / $lon")
        }
    }

    override fun onCreate() {
        locationProvider = LocationProvider(this, locationListener)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val result = START_NOT_STICKY
        when (intent.action) {
            START -> {
                startForeground(
                    EXPLORER_NOTIFICATION_ID,
                    NotificationHelper.buildNotification(this)
                )
                locationProvider.start()
            }
            STOP -> {
                locationProvider.stop()
                stopSelf()
            }
        }
        return result
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "LocationService"
    }
}
