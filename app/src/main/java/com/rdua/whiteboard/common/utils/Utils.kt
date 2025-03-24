package com.rdua.whiteboard.common.utils

import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.scale

fun scaleDownBitmap(bitmap: Bitmap): Bitmap {
    val maxDimension = 300
    val newSize = if (bitmap.width < bitmap.height) {
        Size(
            maxDimension,
            (bitmap.height * maxDimension) / bitmap.width
        )
    } else {
        Size(
            (bitmap.width * maxDimension) / bitmap.height,
            maxDimension
        )
    }
    return bitmap.scale(newSize.width, newSize.height) // uses bilinear filtering by default
}