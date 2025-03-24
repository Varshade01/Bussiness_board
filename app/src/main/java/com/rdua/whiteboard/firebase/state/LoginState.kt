package com.rdua.whiteboard.firebase.state

sealed interface LoginState {
    object Success : LoginState
    object InvalidCredentials : LoginState
    object NetworkFailure : LoginState
    object UnknownFailure : LoginState
}