package com.rdua.whiteboard.settings.state

sealed interface LogoutState {

    object LogoutSuccess : LogoutState

    object LogoutFailure : LogoutState
}
