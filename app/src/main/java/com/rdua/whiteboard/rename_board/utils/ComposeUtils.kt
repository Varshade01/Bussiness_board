package com.rdua.whiteboard.rename_board.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
internal fun stringResource(resource: StringResource?): String? = resource?.let {
    stringResource(resource.id, *resource.args)
}