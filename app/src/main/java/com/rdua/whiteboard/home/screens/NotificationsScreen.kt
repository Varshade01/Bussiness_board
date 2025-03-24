package com.rdua.whiteboard.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.HomeTopBar


@Composable
fun NotificationsScreen() {
    HomeTopBar(titleTopBar = stringResource(R.string.notifications_title))
}