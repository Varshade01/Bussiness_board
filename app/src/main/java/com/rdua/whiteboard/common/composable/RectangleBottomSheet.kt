package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RectangleBottomSheet(
    openBottomSheet: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit
) {
    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RectangleShape,
            //to draw scrim on statusBar
            windowInsets = WindowInsets.navigationBars,
            dragHandle = null,
            content = content
        )
    }
}