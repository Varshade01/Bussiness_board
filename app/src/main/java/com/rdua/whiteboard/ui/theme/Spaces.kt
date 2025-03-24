package com.rdua.whiteboard.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spaces(
    val space: Dp,
    val space1: Dp,
    val space2: Dp,
    val space3: Dp,
    val space4: Dp,
    val space5: Dp,
    val space6: Dp,
    val space10: Dp,
    val space12: Dp,
)

// Default phone spaces
fun defaultSpaces(
    space: Dp = 1.dp,
    space1: Dp = 4.dp,
    space2: Dp = 8.dp,
    space3: Dp = 12.dp,
    space4: Dp = 16.dp,
    space5: Dp = 20.dp,
    space6: Dp = 24.dp,
    space10: Dp = 40.dp,
    space12: Dp = 48.dp,
): Spaces = Spaces(
    space,
    space1,
    space2,
    space3,
    space4,
    space5,
    space6,
    space10,
    space12,
)

internal val LocalSpaces = staticCompositionLocalOf { defaultSpaces() }