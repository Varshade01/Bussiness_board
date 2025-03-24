package com.rdua.whiteboard.settings.state

data class SettingsUIState(
    val isNotificationEnabled: Boolean = false,
    val isOpenLogoutDialog: Boolean = false,
    val isOpenDeleteDialog: Boolean = false,
    val showProgressDeleteAccount: Boolean = false,
)