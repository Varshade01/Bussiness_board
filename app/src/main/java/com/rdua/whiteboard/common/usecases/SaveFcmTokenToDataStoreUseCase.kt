package com.rdua.whiteboard.common.usecases

interface SaveFcmTokenToDataStoreUseCase {
    fun saveFcmToken(token: String)
}