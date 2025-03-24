package com.rdua.whiteboard.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.ApproveDialog
import com.rdua.whiteboard.common.composable.ExtendedText
import com.rdua.whiteboard.common.composable.HomeTopBar
import com.rdua.whiteboard.common.composable.PasswordDialog
import com.rdua.whiteboard.common.composable.ProgressIndicator
import com.rdua.whiteboard.common.state.PasswordState
import com.rdua.whiteboard.settings.event.SettingsEvent
import com.rdua.whiteboard.settings.state.SettingsUIState
import com.rdua.whiteboard.settings.viewmodel.SettingsViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    if (viewModel.state.isOpenLogoutDialog) {
        ApproveDialog(
            titleDialog = stringResource(R.string.logout_title),
            textDialog = stringResource(R.string.text_dialog_logout),
            textConfirmButton = stringResource(R.string.logout_title),
            confirmOnClick = { viewModel.onEvent(SettingsEvent.LogoutEvent) },
            dismissOnClick = { viewModel.onEvent(SettingsEvent.DismissLogoutDialogEvent) }
        )
    }
    if (viewModel.state.isOpenDeleteDialog) {
        DeleteAccountDialog(
            passwordState = viewModel.passwordDialogState,
            onEvent = viewModel::onEvent
        )
    }
    SettingsScreenContent(viewModel.state, viewModel::onEvent)
}

@Composable
private fun SettingsScreenContent(
    state: SettingsUIState,
    onEvent: (SettingsEvent) -> Unit = {},
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            HomeTopBar(titleTopBar = stringResource(id = R.string.settings_title))
            Spacer(modifier = Modifier.height(MaterialTheme.spaces.space3))
            ItemSettings(SettingItem.ChangePasswordItem, onEvent)
            PushNotificationSwitchItem(PushNotificationItem(state.isNotificationEnabled), onEvent)
            ItemSettings(SettingItem.DeleteAccountItem, onEvent)
            ItemSettings(SettingItem.LogoutItem, onEvent)
        }
        if (state.showProgressDeleteAccount) {
            ProgressIndicator()
        }
    }
}

@Composable
private fun DeleteAccountDialog(
    passwordState: PasswordState,
    onEvent: (SettingsEvent) -> Unit,
) {
    var showPasswordDialog by remember { mutableStateOf(false) }
    ApproveDialog(
        titleDialog = stringResource(R.string.delete_my_account),
        textDialog = stringResource(R.string.text_dialog_delete),
        textConfirmButton = stringResource(R.string.delete_dialog_btn),
        confirmOnClick = {
            showPasswordDialog = true
        },
        dismissOnClick = { onEvent(SettingsEvent.DismissDeleteDialogEvent) }
    )
    if (showPasswordDialog) {
        PasswordDialog(
            titleDialog = stringResource(R.string.delete_my_account),
            textConfirmButton = stringResource(R.string.delete_dialog_btn),
            passwordState = passwordState,
            confirmOnClick = {
                onEvent(SettingsEvent.DeleteAccountDialogEvent)
            },
            dismissOnClick = { onEvent(SettingsEvent.DismissDeleteDialogEvent) },
            onValueChange = { onEvent(SettingsEvent.EnterPasswordEvent(it)) },
        )
    }
}

@Composable
private fun ItemSettings(
    settingItem: SettingItem,
    onEvent: (SettingsEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEvent(settingItem.event) }
    ) {
        ExtendedText(
            modifier = Modifier
                .height(MaterialTheme.spaces.space12)
                .padding(horizontal = MaterialTheme.spaces.space3),
            text = stringResource(settingItem.titleId),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            leadingContent = {
                Icon(
                    painter = painterResource(settingItem.iconId),
                    contentDescription = stringResource(settingItem.titleId),
                    modifier = Modifier.padding(horizontal = MaterialTheme.spaces.space1)
                )
            }
        )
    }
}

@Composable
private fun PushNotificationSwitchItem(
    notificationItem: PushNotificationItem,
    onEvent: (SettingsEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.spaces.space12)
            .padding(horizontal = MaterialTheme.spaces.space3),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExtendedText(
            text = stringResource(notificationItem.titleId),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            leadingContent = {
                Icon(
                    painter = painterResource(notificationItem.iconId),
                    contentDescription = stringResource(notificationItem.titleId),
                    modifier = Modifier.padding(horizontal = MaterialTheme.spaces.space1)
                )
            }
        )

        Switch(
            checked = notificationItem.isChecked,
            onCheckedChange = { newCheckedState ->
                onEvent(SettingsEvent.PushNotificationEvent(newCheckedState))
            },
            thumbContent = {
                Icon(
                    imageVector = if (notificationItem.isChecked) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
