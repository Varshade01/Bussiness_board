package com.rdua.whiteboard.registration.usecases

import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.registration.model.RegistrationFields

interface ICreateUserUseCase {

    suspend fun createUser(
        registrationFields: RegistrationFields,
    ): RegistrationState
    suspend fun createTemporaryUser(email:String):RegistrationState
}