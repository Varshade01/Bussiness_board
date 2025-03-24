package com.rdua.whiteboard.login.usecases

import com.rdua.whiteboard.firebase.state.LoginState
import com.rdua.whiteboard.login.model.LoginFields

interface LoginUseCase {
    suspend operator fun invoke(fields: LoginFields): LoginState
}