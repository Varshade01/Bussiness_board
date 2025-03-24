package com.rdua.whiteboard.change_password.events

interface ChangePasswordEvent {

    object ChangePassword : ChangePasswordEvent

    data class CurrentPasswordChange(val newValue: String) : ChangePasswordEvent

    data class NewPasswordChange(val newValue: String) : ChangePasswordEvent

    data class PasswordConfirmChange(val newValue: String) : ChangePasswordEvent

    object NavigateBack : ChangePasswordEvent
}