package com.rdua.whiteboard.change_password.usecase

import com.rdua.whiteboard.firebase.state.ChangePasswordState

interface ChangePasswordUseCase {

    suspend fun changePassword(currentPassword: String, newPassword: String): ChangePasswordState
}