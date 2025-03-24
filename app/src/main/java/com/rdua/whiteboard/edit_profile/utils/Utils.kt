package com.rdua.whiteboard.edit_profile.utils

import android.graphics.Bitmap
import com.rdua.whiteboard.common.utils.scaleDownBitmap
import java.io.ByteArrayOutputStream

fun compressBitmap(bitmap: Bitmap): ByteArray {
    val scaledBitmap = scaleDownBitmap(bitmap)
    val bytes = ByteArrayOutputStream()
    // Using compress to convert Bitmap to ByteArray
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    return bytes.toByteArray()
}