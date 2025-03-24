package com.rdua.whiteboard.board.constants

import androidx.compose.ui.graphics.Color
import com.rdua.whiteboard.ui.theme.Blue65
import com.rdua.whiteboard.ui.theme.Blue70
import com.rdua.whiteboard.ui.theme.Blue80
import com.rdua.whiteboard.ui.theme.Green50
import com.rdua.whiteboard.ui.theme.Green70
import com.rdua.whiteboard.ui.theme.Orange70
import com.rdua.whiteboard.ui.theme.Red70
import com.rdua.whiteboard.ui.theme.Red80
import com.rdua.whiteboard.ui.theme.Yellow70

object DefaultColors {
    val stickyColor = Yellow70
    val shapeColor = Red70
    val shapeBorderColor = Color.Black
    val frameColor = Color.White
    val frameBorderColor = Color.Black
    val lineColor = Color.Black
    val stickyFontColor = Color.Black
    val textFontColor = Color.Black
    val shapeFontColor = Color.White

    private val palette = listOf(
        Color.Black, Red70, Blue70, Yellow70, Green70,
        Blue65, Green50, Orange70, Blue80, Red80, Color.Gray,
    )
    private val textPalette = listOf(
        Color.Black, Color.White, Red70, Blue70, Yellow70, Green70,
        Blue65, Green50, Orange70, Blue80, Red80, Color.Gray,
    )

    val shapeBorderColors = palette
    val shapeColors = palette.placeFirst(shapeColor)
    val stickyColors = palette.placeFirst(stickyColor)
    val frameBorderColors = palette
    val frameColors = palette.add(Color.Transparent).placeFirst(frameColor)
    val lineColors = palette

    val shapeFontColors = textPalette.placeFirst(shapeFontColor)
    val stickyFontColors = textPalette
    val textFontColors = textPalette

    /**
     * Places [value] as the first element in this list. The resulting list will not have duplicates.
     */
    private fun <T> List<T>.placeFirst(value: T): List<T> {
        return linkedSetOf(value).apply {
            addAll(this@placeFirst)
        }.toList()
    }

    /**
     * Adds [value] as the first element in this list.
     */
    private fun <T> List<T>.add(value: T): List<T> {
        return toMutableList().apply {
            add(0, value)
        }
    }
}
