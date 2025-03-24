package com.rdua.whiteboard.board_item.screen.toolbar.color_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rdua.whiteboard.common.composable.ColorButton
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun ColorRangeBar(
    colorOption: ColorOption,
    colors: List<Color>,
    onSelectColor: (Color) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .padding(
                bottom = MaterialTheme.spaces.space3,
                start = MaterialTheme.spaces.space1,
                end = MaterialTheme.spaces.space1
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(MaterialTheme.spaces.space2)
            ),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spaces.space3,
            vertical = MaterialTheme.spaces.space2
        ),
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.spaces.space4,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        items(colors) { item ->
            ColorButton(colorOption = colorOption, color = item, onClick = { onSelectColor(item) })
        }
    }
}
