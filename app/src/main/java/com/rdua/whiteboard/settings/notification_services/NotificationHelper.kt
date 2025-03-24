package com.rdua.whiteboard.settings.notification_services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
    }
}






