package com.rdua.whiteboard.settings.notification_services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rdua.whiteboard.R
import com.rdua.whiteboard.data.data_store.SettingsDataStore
import com.rdua.whiteboard.firebase.utils.FCMconstants.BODY_NOTIFICATION
import com.rdua.whiteboard.firebase.utils.FCMconstants.CHANNEL_ID
import com.rdua.whiteboard.firebase.utils.FCMconstants.NOTIFICATION_ID
import com.rdua.whiteboard.firebase.utils.FCMconstants.TITLE_NOTIFICATION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

@HiltWorker
class PushNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val settingsDataStore: SettingsDataStore
) :
    Worker(appContext, params) {
    override fun doWork(): Result {
        return try {
            val title = inputData.getString(TITLE_NOTIFICATION)
            val body = inputData.getString(BODY_NOTIFICATION)

            val notificationState: Boolean = runBlocking {
                settingsDataStore.isNotificationEnabled()
            }

            if (notificationState && title != null && body != null) {
                createAndShowNotifications(title, body)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun createAndShowNotifications(title: String, body: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notifications)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
