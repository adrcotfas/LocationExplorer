package com.adrcotfas.locationexplorer.business

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.adrcotfas.locationexplorer.START
import com.adrcotfas.locationexplorer.STOP
import com.adrcotfas.locationexplorer.business.NotificationHelper.Companion.EXPLORER_NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationProvider: LocationProvider

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
}