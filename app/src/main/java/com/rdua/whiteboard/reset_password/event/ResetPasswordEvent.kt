package com.rdua.whiteboard.reset_password.event

sealed interface ResetPasswordEvent {

    object ResetPassword : ResetPasswordEvent

    object NavigateToLogin : ResetPasswordEvent

    object NavigateBack : ResetPasswordEvent

    data class EmailChange(val newValue: String) : ResetPasswordEvent
}


