package com.rdua.whiteboard.common.permission

import com.rdua.whiteboard.R

object CameraPermissionInfo : PermissionInfo {
    override val permission: String
        get() = android.Manifest.permission.CAMERA
    override val rationalResourceId: Int
        get() = R.string.camera_permission_rational
}