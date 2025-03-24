package com.rdua.whiteboard.registration.events

sealed interface RegistrationEvent {
    object Register : RegistrationEvent
    object NavigateToLogin : RegistrationEvent
    object NavigateBack : RegistrationEvent
    data class NameChange(val newValue: String) : RegistrationEvent
    data class EmailChange(val newValue: String) : RegistrationEvent
    data class PasswordChange(val newValue: String) : RegistrationEvent
    data class PasswordConfirmChange(val newValue: String) : RegistrationEvent
}