package com.rdua.whiteboard.home.navigation

import com.rdua.whiteboard.R

sealed class HomeScreens(
    val route: String,
    val title: String,
    val iconSelected: Int,
    val iconNotSelected: Int
) {
    object Boards : HomeScreens(
        "home_boards_screen",
        "Boards",
        R.drawable.ic_clipboard,
        R.drawable.ic_clipboard_outlined
    )

    object Notifications : HomeScreens(
        "home_notifications_screen",
        "Notifications",
        R.drawable.ic_notifications,
        R.drawable.ic_notifications_outlined
    )

    object Settings : HomeScreens(
        "home_settings_screen",
        "Settings",
        R.drawable.ic_settings,
        R.drawable.ic_settings_outlined
    )

    object Profile : HomeScreens(
        "home_profile_screen",
        "Profile",
        R.drawable.ic_profile,
        R.drawable.ic_profile_outlined
    )
}