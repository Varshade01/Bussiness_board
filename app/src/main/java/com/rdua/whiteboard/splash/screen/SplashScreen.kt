package com.rdua.whiteboard.splash.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rdua.whiteboard.R
import com.rdua.whiteboard.splash.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {
    SettingStatusBarColor()
    SplashScreenUI()
}

@Composable
private fun SplashScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.background_splash_screen),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Image(
            painter = painterResource(id = R.drawable.logo_splash_screen_white),
            contentDescription = null,
        )
    }
}

@Composable
private fun SettingStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    DisposableEffect(key1 = systemUiController) {
        this.onDispose {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = !isDarkTheme
            )
        }
    }
}

@Composable
@Preview
fun SplashScreenUIPreview() {
    SplashScreenUI()
}
