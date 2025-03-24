package com.rdua.whiteboard.login.events

sealed interface LoginEvent {
    object Login : LoginEvent
    object NavigateToRegister : LoginEvent
    object NavigateToResetPassword : LoginEvent
    class EmailChange(val newValue: String) : LoginEvent
    class PasswordChange(val newValue: String) : LoginEvent
}