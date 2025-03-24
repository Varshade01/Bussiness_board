package com.rdua.whiteboard.common.permission

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {
    val permissionDialogQueue = mutableStateListOf<PermissionInfo>()

    fun dismissDialog() {
        permissionDialogQueue.removeFirst()
    }

    fun onShowPermissionDialog(permission: PermissionInfo) {
        permissionDialogQueue.add(permission)
    }
}