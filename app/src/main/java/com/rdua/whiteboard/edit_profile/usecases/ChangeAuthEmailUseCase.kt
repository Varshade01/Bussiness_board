package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.firebase.state.ChangeEmailState

interface ChangeAuthEmailUseCase {
    suspend operator fun invoke(currentPassword: String, newEmail: String): ChangeEmailState
}