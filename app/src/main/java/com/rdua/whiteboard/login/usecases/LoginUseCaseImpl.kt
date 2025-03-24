package com.rdua.whiteboard.login.usecases

import com.rdua.whiteboard.common.usecases.UpdateFcmTokenToFirebaseUseCaseImpl
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.LoginState
import com.rdua.whiteboard.login.model.LoginFields
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthApi,
    private val usersRepository: UsersRepository,
    private val updateFcmTokenToFirebaseUseCaseImpl: UpdateFcmTokenToFirebaseUseCaseImpl
) : LoginUseCase {
    override suspend operator fun invoke(fields: LoginFields): LoginState {
        val loginState = firebaseAuth.login(fields.email, fields.password)
        if (loginState is LoginState.Success) {
            usersRepository.getUser()
            updateFcmTokenToFirebaseUseCaseImpl.updateFcmToken()
        }
        return loginState
    }
}