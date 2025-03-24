package com.rdua.whiteboard.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStoreImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : SettingsDataStore {

    override suspend fun isNotificationEnabled(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[NOTIFICATION_STATE_KEY] ?: true
    }

    override suspend fun saveNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_STATE_KEY] = enabled
        }
    }

    override suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { store -> store[FCM_TOKEN_KEY] = token }
    }

    override suspend fun getFcmToken(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[FCM_TOKEN_KEY]
    }

    override suspend fun clearNotificationSwitchState() {
        context.dataStore.edit { preferences ->
            preferences.remove(NOTIFICATION_STATE_KEY)
        }
    }

    companion object {
        val NOTIFICATION_STATE_KEY = booleanPreferencesKey("notification_state")
        val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    }
}