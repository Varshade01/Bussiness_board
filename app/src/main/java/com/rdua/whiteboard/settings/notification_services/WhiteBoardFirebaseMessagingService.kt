package com.rdua.whiteboard.settings.notification_services

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rdua.whiteboard.common.usecases.RemoveFcmTokenUseCase
import com.rdua.whiteboard.common.usecases.SaveFcmTokenToDataStoreUseCase
import com.rdua.whiteboard.common.usecases.UpdateFcmTokenToFirebaseUseCase
import com.rdua.whiteboard.firebase.utils.FCMconstants.BODY_NOTIFICATION
import com.rdua.whiteboard.firebase.utils.FCMconstants.TITLE_NOTIFICATION
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WhiteBoardFirebaseMessagingService
    : FirebaseMessagingService() {

    @Inject
    lateinit var updateFcmTokenToFirebaseUseCase: UpdateFcmTokenToFirebaseUseCase

    @Inject
    lateinit var saveFcmTokenToDataStoreUseCase: SaveFcmTokenToDataStoreUseCase

    @Inject
    lateinit var removeFcmTokenUseCase: RemoveFcmTokenUseCase

    override fun onNewToken(token: String) {
        removeFcmTokenUseCase.removeFcmToken()
        saveFcmTokenToDataStoreUseCase.saveFcmToken(token)
        updateFcmTokenToFirebaseUseCase.updateFcmToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyTAG", "Message data payload: ${remoteMessage.data}")
        }

        val notificationTitle = remoteMessage.notification?.title
        val notificationBody = remoteMessage.notification?.body

        val inputData = Data.Builder()
            .putString(TITLE_NOTIFICATION, notificationTitle)
            .putString(BODY_NOTIFICATION, notificationBody)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = OneTimeWorkRequestBuilder<PushNotificationWorker>()
            .setInputData(inputData)
            .build()

        workManager.enqueue(workRequest)
    }
}

