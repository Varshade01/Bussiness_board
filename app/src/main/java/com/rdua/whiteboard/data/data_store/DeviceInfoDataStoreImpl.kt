package com.rdua.whiteboard.data.data_store

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class DeviceInfoDataStoreImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : DeviceInfoDataStore {
    private val Context.dataStore by preferencesDataStore(name = "device_info")

    override suspend fun getDeviceId(): String {
        var deviceId = context.dataStore.data
            .map { preferences -> preferences[DEVICE_ID] ?: "" }
            .first()

        if (deviceId.isBlank()) { // No device id in dataStore.
            deviceId = context.getAndroidDeviceId()

            if (deviceId.isBlank()) { // Can't get System device id.
                deviceId = UUID.randomUUID().toString()
            }
            saveDeviceId(deviceId)
        }
        return deviceId
    }

    override suspend fun saveDeviceId(id: String) {
        context.dataStore.edit { store -> store[DEVICE_ID] = id }
    }

    @SuppressLint("HardwareIds")
    private fun Context.getAndroidDeviceId(): String {
        return try {
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        private val DEVICE_ID = stringPreferencesKey("device_id")
    }
}