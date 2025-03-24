package com.rdua.whiteboard.di

import com.rdua.whiteboard.data.data_store.DeviceInfoDataStore
import com.rdua.whiteboard.data.data_store.DeviceInfoDataStoreImpl
import com.rdua.whiteboard.data.data_store.SettingsDataStore
import com.rdua.whiteboard.data.data_store.SettingsDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Singleton
    @Binds
    abstract fun bindSettingsDataStore(settingsDataStoreImpl: SettingsDataStoreImpl): SettingsDataStore

    @Singleton
    @Binds
    abstract fun bindDeviceInfoDataStore(deviceInfoDataStoreImpl: DeviceInfoDataStoreImpl): DeviceInfoDataStore
}