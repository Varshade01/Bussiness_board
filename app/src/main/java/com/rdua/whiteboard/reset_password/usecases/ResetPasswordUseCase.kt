package com.rdua.whiteboard.reset_password.usecases

import com.rdua.whiteboard.firebase.state.ResetPasswordState

interface ResetPasswordUseCase {

    suspend fun resetPassword(email: String): ResetPasswordState
}