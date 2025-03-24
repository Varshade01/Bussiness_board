package com.rdua.whiteboard.data.data_store

interface SettingsDataStore {
    suspend fun isNotificationEnabled(): Boolean
    suspend fun clearNotificationSwitchState()
    suspend fun saveNotificationEnabled(enabled: Boolean)
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String?
}