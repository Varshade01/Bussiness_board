package com.rdua.whiteboard.common.usecases

interface UpdateFcmTokenToFirebaseUseCase {
    fun updateFcmToken(token: String? = null)
}