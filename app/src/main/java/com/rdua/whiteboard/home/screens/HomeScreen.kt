package com.rdua.whiteboard.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rdua.whiteboard.common.composable.HomeDivider
import com.rdua.whiteboard.common.composable.WhiteboardSnackbarHost
import com.rdua.whiteboard.common.manager.snackbar.rememberSnackbarFlowState
import com.rdua.whiteboard.home.navigation.HomeScreens
import com.rdua.whiteboard.home.viewmodel.HomeViewModel
import com.rdua.whiteboard.profile_screen_non_edit.screen.ProfileScreen
import com.rdua.whiteboard.settings.screen.SettingsScreen
import com.rdua.whiteboard.ui.theme.MaterialTheme


private val bottomNavigationItems = listOf(
    HomeScreens.Boards,
    HomeScreens.Notifications,
    HomeScreens.Settings,
    HomeScreens.Profile
)

@Preview
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val snackbarHostState = rememberSnackbarFlowState(
        snackbarFlow = viewModel.snackbarFlow
    )

    Scaffold(
        modifier = Modifier
            // Background is white by default
            .navigationBarsPadding(),
        bottomBar = {
            Column {

                HomeDivider()

                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp
                ) {
                    bottomNavigationItems.forEach { screen ->

                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        BottomNavigationItem(
                            selected = isSelected,
                            selectedContentColor = MaterialTheme.colors.primary,
                            unselectedContentColor = Color.Gray,
                            onClick = {
                                navController.navigate(screen.route) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = if (isSelected) screen.iconSelected else screen.iconNotSelected
                                    ),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    style = MaterialTheme.typography.labelSmall,
                                    text = screen.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            alwaysShowLabel = true
                        )
                    }
                }

            }
        },
        snackbarHost = {
            WhiteboardSnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->

        NavHost(
            navController,
            startDestination = HomeScreens.Boards.route,
            Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(HomeScreens.Boards.route) {
                AllBoardsScreen()
            }
            composable(HomeScreens.Profile.route) {
                ProfileScreen()
            }
            composable(HomeScreens.Notifications.route) {
                NotificationsScreen()
            }
            composable(HomeScreens.Settings.route) {
                SettingsScreen()
            }
        }
    }
}