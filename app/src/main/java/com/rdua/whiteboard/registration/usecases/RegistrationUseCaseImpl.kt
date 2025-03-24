package com.rdua.whiteboard.registration.usecases

import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.registration.model.RegistrationFields
import javax.inject.Inject

class RegistrationUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthApi,
) : IRegistrationUseCase {

    override suspend fun registerUser(
        fields: RegistrationFields
    ): RegistrationState = firebaseAuth.register(fields.email, fields.password)

}
