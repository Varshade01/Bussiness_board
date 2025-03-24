package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.permission.PermissionInfo
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun PermissionDialog(
    permissionInfo: PermissionInfo,
    onDismissRequest: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.permission_required))
        },
        text = {
            Text(text = stringResource(permissionInfo.rationalResourceId))
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.settings),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spaces.space2)
                    .clickable(onClick = onGoToAppSettingsClick)
            )
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.not_now),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spaces.space2)
                    .clickable(onClick = onDismissRequest)
            )
        }
    )
}