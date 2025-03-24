package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun BottomSheetTextItem(
    text: String,
    modifier: Modifier = Modifier,
    height: Dp = 50.dp,
    leadingIconPadding: Dp = 10.dp,
    onClick: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick?.invoke()
            }
    ) {
        ExtendedText(
            text = text,
            leadingContent = leadingIcon,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            horizontalArrangement = Arrangement.spacedBy(leadingIconPadding),
            modifier = Modifier
                .height(height)
                .padding(horizontal = MaterialTheme.spaces.space2)
        )
    }
}