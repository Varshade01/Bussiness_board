package com.rdua.whiteboard.settings.screen

import com.rdua.whiteboard.R
import com.rdua.whiteboard.settings.event.SettingsEvent

sealed class SettingItem(
    val iconId: Int,
    val titleId: Int,
    val event: SettingsEvent,
) {
    object ChangePasswordItem : SettingItem(
        iconId = R.drawable.ic_close_lock,
        titleId = R.string.change_password,
        event = SettingsEvent.ChangePasswordEvent
    )

    object DeleteAccountItem : SettingItem(
        iconId = R.drawable.ic_trash,
        titleId = R.string.delete_my_account,
        event = SettingsEvent.OpenDeleteAccountDialogEvent
    )

    object LogoutItem : SettingItem(
        iconId = R.drawable.ic_close_lock,
        titleId = R.string.logout,
        event = SettingsEvent.OpenLogoutDialogEvent
    )
}
