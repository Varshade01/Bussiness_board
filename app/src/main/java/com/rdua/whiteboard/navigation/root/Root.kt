package com.rdua.whiteboard.navigation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rdua.whiteboard.edit_profile.navigation.editProfileScreen
import com.rdua.whiteboard.change_password.navigation.changePasswordScreen
import com.rdua.whiteboard.common.manager.toast.collectToasts
import com.rdua.whiteboard.home.navigation.homeScreen
import com.rdua.whiteboard.login.navigation.loginScreen
import com.rdua.whiteboard.navigation.LocalNavController
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.getNavController
import com.rdua.whiteboard.navigation.navigator.NavigationIntent
import com.rdua.whiteboard.navigation.viewmodel.NavigationViewModel
import com.rdua.whiteboard.navigation.viewmodel.ToastViewModel
import com.rdua.whiteboard.board_item.navigation.boardItemScreen
import com.rdua.whiteboard.profile_screen_non_edit.navigation.profileScreen
import com.rdua.whiteboard.registration.navigation.registrationScreen
import com.rdua.whiteboard.reset_password.navigation.resetPasswordScreen
import com.rdua.whiteboard.splash.navigation.splashScreen
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun Root(
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    toastViewModel: ToastViewModel = hiltViewModel()
) {
    CompositionLocalProvider(
        LocalNavController provides getNavController()
    ) {
        NavigationEffects(
            navHostController = getNavController(),
            navigationFlow = navigationViewModel.navigationFlow,
        )

        NavHost(
            navController = getNavController(),
            startDestination = Destination.SplashScreen.fullRoute
        ) {
            splashScreen()
            loginScreen()
            registrationScreen()
            homeScreen()
            resetPasswordScreen()
            profileScreen()
            boardItemScreen()
            editProfileScreen()
            changePasswordScreen()
        }
    }

    toastViewModel.toastFlow.collectToasts()
}

@Composable
fun NavigationEffects(
    navHostController: NavHostController,
    navigationFlow: SharedFlow<NavigationIntent>
) {
    LaunchedEffect(navHostController) {
        navigationFlow.collect { intent ->
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}