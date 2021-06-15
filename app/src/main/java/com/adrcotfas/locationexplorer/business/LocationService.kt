package com.adrcotfas.locationexplorer.business

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.adrcotfas.locationexplorer.START
import com.adrcotfas.locationexplorer.STOP
import com.adrcotfas.locationexplorer.business.NotificationHelper.Companion.EXPLORER_NOTIFICATION_ID
import com.adrcotfas.locationexplorer.room.PhotoUrlDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    companion object {
        var isRunning = false
    }

    @Inject
    lateinit var locationProvider: LocationProvider

    @Inject
    lateinit var db: PhotoUrlDatabase

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val result = START_NOT_STICKY
        when (intent.action) {
            START -> {
                startForeground(
                    EXPLORER_NOTIFICATION_ID,
                    NotificationHelper.buildNotification(this)
                )
                locationProvider.start()
                isRunning = true
            }
            STOP -> {
                locationProvider.stop()
                stopSelf()
                isRunning = false
            }
        }
        return result
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
