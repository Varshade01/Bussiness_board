package com.rdua.whiteboard.common.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import com.rdua.whiteboard.BuildConfig
import java.io.File
import java.text.DateFormat.getTimeInstance
import java.util.Objects

fun Context.createTempImageFile(): Uri {
    val image = File.createTempFile(
        "JPEG_${getTimeInstance()}_", //prefix
        ".jpg",  //suffix
        externalCacheDir //directory
    )
    return FileProvider.getUriForFile(
        Objects.requireNonNull(this),
        "${BuildConfig.APPLICATION_ID}.provider", image
    )
}

fun Context.getBitmap(uri: Uri): Bitmap {
    val source = ImageDecoder.createSource(contentResolver, uri)
    return ImageDecoder.decodeBitmap(source)
}

fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}