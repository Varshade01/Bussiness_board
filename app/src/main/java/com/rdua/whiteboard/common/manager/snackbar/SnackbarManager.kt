package com.rdua.whiteboard.common.manager.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides a system to display snackbars using [sendSnackbar] to `emit` [SnackbarEvent]s in a
 * [snackbarFlow].
 *
 * To display a snackbar [Inject] this [Singleton] in the controlling class (ex. ViewModel/Presenter)
 * and give access to [snackbarFlow] to the UI. Collect [SnackbarEvent]s in UI and show snackbars.
 */
@Singleton
class SnackbarManager @Inject constructor() {
    private val _shackbarFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarFlow = _shackbarFlow.asSharedFlow()

    suspend fun sendSnackbar(snackbar: SnackbarEvent) {
        _shackbarFlow.emit(snackbar)
    }
}