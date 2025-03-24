package com.rdua.whiteboard.firebase.state

sealed class ReauthState {

    object ReauthStateSuccess : ReauthState()

    object UserIsNotExist : ReauthState()

    object InvalidCredentials : ReauthState()

    object NetworkFailure : ReauthState()

    object UnknownFailure : ReauthState()

}