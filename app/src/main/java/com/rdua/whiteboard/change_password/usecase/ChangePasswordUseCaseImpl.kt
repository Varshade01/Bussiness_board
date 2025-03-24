package com.rdua.whiteboard.change_password.usecase

import com.rdua.whiteboard.firebase.state.ChangePasswordState
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.ReauthState
import javax.inject.Inject

class ChangePasswordUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthApi
) : ChangePasswordUseCase {
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): ChangePasswordState {

        val reauthState = firebaseAuth.reauthenticate(currentPassword)
        when (reauthState) {
            is ReauthState.ReauthStateSuccess -> {
                return firebaseAuth.changePassword(newPassword)
            }

            is ReauthState.NetworkFailure -> {
                return ChangePasswordState.NetworkFailure
            }

            is ReauthState.InvalidCredentials -> {
                return ChangePasswordState.InvalidCredentials
            }

            is ReauthState.UserIsNotExist -> {
                return ChangePasswordState.UserIsNotExists
            }

            else -> {
                return ChangePasswordState.UnknownFailure
            }
        }
    }
}

