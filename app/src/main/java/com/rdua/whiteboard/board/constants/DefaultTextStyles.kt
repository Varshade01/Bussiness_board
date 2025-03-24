package com.rdua.whiteboard.board.constants

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object DefaultTextStyles {
    val stickyUITextStyle = TextStyle(
        color = DefaultColors.stickyFontColor,
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
    )

    val stickyUIAuthorTextStyle = TextStyle(
        color = Color.Black,
        fontSize = 8.sp,
    )

    val shapeUITextStyle = TextStyle(
        color = DefaultColors.shapeFontColor,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
    )

    val textUITextStyle = TextStyle(
        color = DefaultColors.textFontColor,
        fontSize = 18.sp,
        textAlign = TextAlign.Start,
    )

    val stickyUITextAlignment: Alignment = Alignment.TopCenter
    val shapeUITextAlignment: Alignment = Alignment.Center

    // Font sizes for FontRange toolbar.
    val stickyFontSizes = listOf(4, 8, 10, 12, 14, 18, 24, 32, 48, 64)
    val shapeFontSizes = listOf(10, 12, 14, 18, 24, 36, 48, 64, 80, 144, 288)
    val textFontSizes = shapeFontSizes

    val minFontSize: TextUnit = 4.0.sp

    val absoluteMaxFontSize = 9999.sp
    val stickyMaxFontSize = stickyFontSizes.last().sp
}