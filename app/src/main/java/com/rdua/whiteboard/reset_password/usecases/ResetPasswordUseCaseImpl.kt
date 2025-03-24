package com.rdua.whiteboard.reset_password.usecases

import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.ResetPasswordState
import javax.inject.Inject

class ResetPasswordUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthApi
) : ResetPasswordUseCase {

    override suspend fun resetPassword(email: String): ResetPasswordState =
        firebaseAuth.resetPassword(email)
}