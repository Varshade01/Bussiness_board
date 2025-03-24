package com.rdua.whiteboard.common.composable

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@Composable
fun HomeDivider(
    modifier: Modifier = Modifier,
) {
    Divider(
        color = Color.Black,
        modifier = modifier.alpha(0.2f)
    )
}
