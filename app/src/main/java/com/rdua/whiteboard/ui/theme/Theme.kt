package com.rdua.whiteboard.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private typealias CustomMaterialTheme = com.rdua.whiteboard.ui.theme.MaterialTheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Blue70,
    tertiary = Pink40,
    onSurface = Color.Black,
    onBackground = Color.Black

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val lightColors = defaultColors(
    /* Default colors to override
    primaryVariant = Blue60,
    textVariant = Gray30,
    textFieldAccent = Color.Black,
    statusBar = White99
    */
)

private val darkColors = defaultColors()

private val DefaultSpaces = defaultSpaces(
    /* Spaces to override
    space1 = 4.dp,
    space2 = 8.dp,
    space3 = 12.dp,
    space4 = 16.dp,
    space5 = 20.dp,
    space6 = 24.dp
     */
)


@Composable
fun WhiteBoardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colors = when {
        darkTheme -> darkColors
        else -> lightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.statusBar.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        colors = colors,
        spaces = DefaultSpaces,
        typography = Typography,
        content = content
    )
}

@Composable
fun MaterialTheme(
    colorScheme: ColorScheme = CustomMaterialTheme.colorScheme,
    colors: Colors = CustomMaterialTheme.colors,
    spaces: Spaces = CustomMaterialTheme.spaces,
    shapes: Shapes = CustomMaterialTheme.shapes,
    typography: Typography = CustomMaterialTheme.typography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSpaces provides spaces,
        LocalTypography provides typography,
        LocalColors provides colors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            content = content
        )
    }
}