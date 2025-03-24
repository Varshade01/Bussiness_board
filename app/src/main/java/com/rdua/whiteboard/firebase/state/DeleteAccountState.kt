package com.rdua.whiteboard.firebase.state

sealed class DeleteAccountState {

    object DeleteAccountSuccess : DeleteAccountState()

    object UserIsNotExist : DeleteAccountState()

    object InvalidCredentials : DeleteAccountState()

    object NetworkFailure : DeleteAccountState()

    object UnknownFailure : DeleteAccountState()
}
