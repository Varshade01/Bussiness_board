package com.rdua.whiteboard.registration.usecases

import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.registration.model.RegistrationFields
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class CreateUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
    private val registrationUseCase: IRegistrationUseCase
) : ICreateUserUseCase {

    override suspend fun createUser(
        registrationFields: RegistrationFields,
    ): RegistrationState {
        val state = registrationUseCase.registerUser(registrationFields)
        return usersRepository.createUser(state, registrationFields)
    }

    override suspend fun createTemporaryUser(
        email: String,
    ): RegistrationState {
        val registrationFields = buildRegistrationFieldsForTemporaryUserAccount(email)
        //register temporary user with default generated info(name,password)
        val temporaryUserState = registrationUseCase.registerUser(registrationFields)
        return if (temporaryUserState is RegistrationSuccess) {
            usersRepository.createUser(temporaryUserState, registrationFields)
        } else {
            temporaryUserState
        }
    }

    private fun buildRegistrationFieldsForTemporaryUserAccount(email: String): RegistrationFields {
        val name = getTemporaryName(email) //  temporary name by email
        val password = generateTemporaryPassword() //  get temporary password
        return RegistrationFields(
            name = name,
            email = email,
            password = password,
            temporaryUser = true
        )
    }

    private fun getTemporaryName(email: String): String {
        // get name before @ from email
        return email.substringBefore('@')
    }

    private fun generateTemporaryPassword(): String {
        // generate temporary password for temp account
        return "defaultPassword"
    }
}
