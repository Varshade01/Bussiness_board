package com.rdua.whiteboard.firebase.state

sealed class ChangePasswordState {

    object ChangePasswordSuccess : ChangePasswordState()

    object UserIsNotExists : ChangePasswordState()

    object InvalidCredentials : ChangePasswordState()

    object NetworkFailure : ChangePasswordState()

    object UnknownFailure : ChangePasswordState()
}