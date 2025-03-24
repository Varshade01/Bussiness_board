package com.rdua.whiteboard.settings.event

sealed interface SettingsEvent {

    object ChangePasswordEvent : SettingsEvent
    object OpenDeleteAccountDialogEvent : SettingsEvent
    object OpenLogoutDialogEvent : SettingsEvent
    object LogoutEvent : SettingsEvent
    object DeleteAccountDialogEvent : SettingsEvent
    object DismissDeleteDialogEvent : SettingsEvent
    object DismissLogoutDialogEvent : SettingsEvent
    data class EnterPasswordEvent(val password: String) : SettingsEvent
    data class PushNotificationEvent(val isChecked: Boolean) : SettingsEvent
}
