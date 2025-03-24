package com.rdua.whiteboard.home.viewmodel

import androidx.lifecycle.ViewModel
import com.rdua.whiteboard.common.manager.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
): ViewModel() {
    val snackbarFlow = snackbarManager.snackbarFlow
}