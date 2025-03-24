package com.rdua.whiteboard.data.data_store

interface DeviceInfoDataStore {
    suspend fun getDeviceId(): String
    suspend fun saveDeviceId(id: String)
}