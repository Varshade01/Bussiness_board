package com.rdua.whiteboard.settings.usecase

import com.rdua.whiteboard.settings.state.LogoutState

interface ILogoutUseCase {
    suspend fun logoutUser(): LogoutState
}