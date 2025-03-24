package com.rdua.whiteboard.common.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackButtonTopBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = { },
        navigationIcon = { BackButton(onBackPressed) }
    )
}