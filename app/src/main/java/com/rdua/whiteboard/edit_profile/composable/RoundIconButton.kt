package com.rdua.whiteboard.edit_profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    touchSize: Dp = 48.dp,
    iconSize: Dp = 28.dp,
    backgroundColor: Color = MaterialTheme.colors.iconButton,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(touchSize)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}