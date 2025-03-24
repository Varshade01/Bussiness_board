package com.rdua.whiteboard.data.entities.boards.wrappers

import androidx.compose.ui.text.style.TextAlign

fun TextAlign.toJson() = this.toString()

// TextAlign is a value class, not enum.
fun String.fromTextAlignJson() = TextAlign.values().find { it.toString() == this }