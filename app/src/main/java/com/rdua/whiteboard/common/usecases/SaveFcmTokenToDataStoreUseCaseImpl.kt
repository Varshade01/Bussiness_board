package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.data.data_store.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SaveFcmTokenToDataStoreUseCaseImpl(
    private val settingsDataStore: SettingsDataStore,
    private val coroutineScope: CoroutineScope,

    ) : SaveFcmTokenToDataStoreUseCase {
    override fun saveFcmToken(token: String) {
        coroutineScope.launch {
            settingsDataStore.saveFcmToken(token)
        }
    }
}
