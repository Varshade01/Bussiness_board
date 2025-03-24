package com.rdua.whiteboard.common.composable

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun WhiteboardSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        Snackbar(
            snackbarData = data,
            actionColor = MaterialTheme.colors.primaryVariant,
        )
    }
}