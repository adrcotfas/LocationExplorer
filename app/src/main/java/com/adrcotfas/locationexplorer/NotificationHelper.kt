package com.adrcotfas.locationexplorer

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(context: Context) : ContextWrapper(context) {

    companion object {
        private const val EXPLORER_CHANNEL_ID = "explorer.notification"
        const val EXPLORER_NOTIFICATION_ID = 42

        fun buildNotification(context: Context): Notification {
            val builder = getBasicNotificationBuilder(context)
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.initIfNeeded()
            return builder.build()
        }

        private fun getBasicNotificationBuilder(context: Context): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, EXPLORER_CHANNEL_ID)
                .setContentText("Exploring the area...")
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setAutoCancel(true)
                .setShowWhen(false)
                .setContentIntent(getPendingIntentWithStack(context))
        }

        private fun getPendingIntentWithStack(context: Context): PendingIntent {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.action = Intent.ACTION_MAIN
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return PendingIntent.getActivity(
                context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun NotificationManager.initIfNeeded() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (this.getNotificationChannel(EXPLORER_CHANNEL_ID) == null) {
                    val channel = NotificationChannel(
                        EXPLORER_CHANNEL_ID, "Location Explorer",
                        NotificationManager.IMPORTANCE_LOW
                    )
                    channel.apply {
                        setBypassDnd(true)
                        setShowBadge(true)
                        setSound(null, null)
                    }
                    this.createNotificationChannel(channel)
                }
            }
        }
    }
}