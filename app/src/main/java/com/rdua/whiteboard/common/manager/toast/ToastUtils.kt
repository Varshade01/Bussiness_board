package com.rdua.whiteboard.common.manager.toast

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow

@SuppressLint("ComposableNaming")
@Composable
fun Flow<ToastEvent>.collectToasts() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        collect { toastEvent ->
            Toast.makeText(
                context,
                toastEvent.messageResId,
                toastEvent.duration
            ).show()
        }
    }
}