package com.rdua.whiteboard.registration.usecases

import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.registration.model.RegistrationFields

interface IRegistrationUseCase {

    suspend fun registerUser(fields: RegistrationFields): RegistrationState
}