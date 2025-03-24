package com.rdua.whiteboard.firebase.state

sealed interface ChangeEmailState {
    object Success : ChangeEmailState
    object UserIsNotExists : ChangeEmailState
    object InvalidCredentials : ChangeEmailState
    object EmailAlreadyTaken : ChangeEmailState
    object NetworkFailure : ChangeEmailState
    object UnknownFailure : ChangeEmailState
}