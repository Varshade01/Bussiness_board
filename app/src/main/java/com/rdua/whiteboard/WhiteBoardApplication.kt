package com.rdua.whiteboard

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.rdua.whiteboard.firebase.utils.FCMconstants.CHANNEL_DESCRIPTION
import com.rdua.whiteboard.firebase.utils.FCMconstants.CHANNEL_ID
import com.rdua.whiteboard.firebase.utils.FCMconstants.CHANNEL_NAME
import com.rdua.whiteboard.settings.notification_services.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WhiteBoardApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION
        )
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}