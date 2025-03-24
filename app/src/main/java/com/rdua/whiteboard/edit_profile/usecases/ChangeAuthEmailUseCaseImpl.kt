package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.firebase.state.ChangeEmailState
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.ReauthState
import javax.inject.Inject

class ChangeAuthEmailUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthApi
) : ChangeAuthEmailUseCase {
    override suspend operator fun invoke(currentPassword: String, newEmail: String): ChangeEmailState {
        return when (firebaseAuth.reauthenticate(currentPassword)) {
            is ReauthState.ReauthStateSuccess -> {
                firebaseAuth.changeEmail(newEmail)
            }

            is ReauthState.NetworkFailure -> ChangeEmailState.NetworkFailure
            is ReauthState.InvalidCredentials -> ChangeEmailState.InvalidCredentials
            is ReauthState.UserIsNotExist -> ChangeEmailState.UserIsNotExists
            else -> ChangeEmailState.UnknownFailure
        }
    }
}