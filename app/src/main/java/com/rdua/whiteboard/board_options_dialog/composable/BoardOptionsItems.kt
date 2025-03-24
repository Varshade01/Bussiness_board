package com.rdua.whiteboard.board_options_dialog.composable

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.BottomSheetTextItem

@Composable
fun RenameBoardItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.rename_board),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_edit_24),
                contentDescription = stringResource(R.string.rename_board_description)
            )
        }
    )
}

@Composable
fun InviteViaEmailItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.invite_via_email),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_email),
                contentDescription = stringResource(R.string.invite_via_email_description)
            )
        }
    )
}

@Composable
fun SharingOptionsItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.sharing_options),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                contentDescription = stringResource(R.string.sharing_options_description)
            )
        }
    )
}

@Composable
fun SharingLinkItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.sharing_link),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_link),
                contentDescription = stringResource(R.string.sharing_link_description)
            )
        }
    )
}

@Composable
fun DuplicateItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.duplicate),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_duplicate),
                contentDescription = stringResource(R.string.duplicate_description)
            )
        }
    )
}

@Composable
fun DeleteBoardItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.delete_board),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.delete_board_description)
            )
        }
    )
}