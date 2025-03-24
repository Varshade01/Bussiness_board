package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.data.data_store.DeviceInfoDataStore
import javax.inject.Inject

class GetDeviceIdUseCaseImpl @Inject constructor(
    private val dataStore: DeviceInfoDataStore,
): GetDeviceIdUseCase {
    override suspend fun invoke(): String {
        return dataStore.getDeviceId()
    }
}