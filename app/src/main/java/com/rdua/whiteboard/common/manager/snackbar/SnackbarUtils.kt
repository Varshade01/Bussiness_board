package com.rdua.whiteboard.common.manager.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Remembers the [SnackbarHostState] that collects a [snackbarFlow] that provides [SnackbarEvent]
 * to display snackbars.
 */
@Composable
fun rememberSnackbarFlowState(
    snackbarFlow: Flow<SnackbarEvent> = flowOf()
): SnackbarHostState {
    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        snackbarFlow.collectLatest { event ->
            when (event) {
                is SnackbarEvent.ShowSnackbar -> {
                    val snackbarResult = snackBarHostState.showSnackbar(
                        message = context.getString(event.messageResId),
                        actionLabel = event.actionLabelResId?.let { resId ->
                            context.getString(resId)
                        },
                        withDismissAction = event.withDismissAction,
                        duration = event.duration,
                    )
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> event.action()
                        SnackbarResult.Dismissed -> { /* do nothing */ }
                    }
                }

                is SnackbarEvent.CancelSnackbar -> Unit // Cancels snackbar with animation
            }
        }
    }
    return snackBarHostState
}