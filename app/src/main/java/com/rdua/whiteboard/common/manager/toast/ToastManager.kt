package com.rdua.whiteboard.common.manager.toast

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToastManager @Inject constructor() {
    private val _toastFlow = MutableSharedFlow<ToastEvent>()
    val toastFlow = _toastFlow.asSharedFlow()

    suspend fun sendToast(toast: ToastEvent) {
        _toastFlow.emit(toast)
    }
}