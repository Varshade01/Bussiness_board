package com.rdua.whiteboard.settings.usecase

import com.rdua.whiteboard.firebase.state.DeleteAccountState

interface DeleteAccountUseCase {
    suspend fun deleteAccount(password: String): DeleteAccountState
}