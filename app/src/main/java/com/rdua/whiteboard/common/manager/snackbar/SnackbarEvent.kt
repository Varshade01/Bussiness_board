package com.rdua.whiteboard.common.manager.snackbar

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration

sealed interface SnackbarEvent {
    /**
     * Encapsulates data for a snackbar event.
     *
     * @param messageResId a reference to a [String] resource.
     * @param actionLabelResId an optional reference to a [String] resource for a snackbar action.
     * @param action is called when user click on action button if [actionLabelResId] was provided.
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar.
     * @param duration duration to control how long snackbar will be shown,
     * either [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     */
    class ShowSnackbar(
        @StringRes val messageResId: Int,
        @StringRes val actionLabelResId: Int? = null,
        val action: suspend () -> Unit = { },
        val withDismissAction: Boolean = false,
        val duration: SnackbarDuration = SnackbarDuration.Short,
    ) : SnackbarEvent

    /**
     * Provides a way to cancel current snackbar.
     */
    object CancelSnackbar : SnackbarEvent
}