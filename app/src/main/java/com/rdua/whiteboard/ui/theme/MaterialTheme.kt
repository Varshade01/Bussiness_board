package com.rdua.whiteboard.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

object MaterialTheme {
    val spaces: Spaces
        @Composable
        @ReadOnlyComposable
        get() = LocalSpaces.current

    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}

internal val LocalShapes = staticCompositionLocalOf { Shapes() }
internal val LocalTypography = staticCompositionLocalOf { Typography() }
internal val LocalColorScheme = staticCompositionLocalOf { lightColorScheme() }