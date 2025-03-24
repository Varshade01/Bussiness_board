package com.rdua.whiteboard.data.entities.boards.wrappers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toColorJson(): String = Integer.toHexString(this.toArgb())

fun String.fromColorJson(): Color? {
    val color =  try {
        toLong(16)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return color?.let { Color(color) }
}