package com.rdua.whiteboard.board_item.screen.toolbar.text_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.model.BoardTextItemAutoFontModel
import com.rdua.whiteboard.board.model.BoardTextItemModel
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.board_item.event.TextToolbarEvent
import com.rdua.whiteboard.board_item.viewmodel.BoardItemViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme


@Composable
fun FontSizeRangeBar(
    viewModel: BoardItemViewModel,
    model: BoardTextItemModel,
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
            horizontal = MaterialTheme.spaces.space2,
            vertical = MaterialTheme.spaces.space2
        ),
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.spaces.space3,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        if (model is BoardTextItemAutoFontModel) {
            item {
                AutoFontItemUI(
                    isAutoEnabled = model.isAutoFontSizeMode,
                    curMaxSize = model.maxFontSize.value.toInt(),
                    onSelectFontSize = { fontSize ->
                        viewModel.onEvent(TextToolbarEvent.ChangeFontSize(fontSize = fontSize.sp))
                    },
                    onSetAutoMode = { isEnable ->
                        viewModel.onEvent(TextToolbarEvent.SetAutoMode(isEnable))
                    },
                )
            }
        }

        items(getFontSizes(model, viewModel)) { size ->
            FontSizeItemUI(
                fontSize = size,
                curMaxSize = model.maxFontSize.value.toInt(),
                onSelectFontSize = { fontSize ->
                    viewModel.onEvent(TextToolbarEvent.ChangeFontSize(fontSize = fontSize.sp))
                },
                onSetAutoMode = { isEnable ->
                    viewModel.onEvent(TextToolbarEvent.SetAutoMode(isEnable))
                },
            )
        }
    }
}

@Composable
private fun AutoFontItemUI(
    isAutoEnabled: Boolean,
    curMaxSize: Int,
    onSelectFontSize: (fontSize: Int) -> Unit = { },
    onSetAutoMode: (isEnable: Boolean) -> Unit = { },
) {
    val modifier = Modifier
        // When AutoMode on, highlight it.
        .background(
            color = if (isAutoEnabled) Color.LightGray else Color.Transparent
        )
        .clickable {
            // Toggle automode when auto button is pressed.
            onSetAutoMode(!isAutoEnabled)
            // Sending event will ensure backstack entry and remote synchronization.
            if (!isAutoEnabled) {
                onSelectFontSize(curMaxSize)
            }
        }

    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = stringResource(R.string.auto),
    )
}

@Composable
private fun FontSizeItemUI(
    fontSize: Int,
    curMaxSize: Int,
    onSelectFontSize: (fontSize: Int) -> Unit = { },
    onSetAutoMode: (isEnable: Boolean) -> Unit = { },
) {
    val modifier = Modifier
        .widthIn(min = MaterialTheme.spaces.space5)
        .clickable(enabled = fontSize <= curMaxSize) {
            // Disable auto mode if specific font size is chosen
            onSetAutoMode(false)
            onSelectFontSize(fontSize)
        }

    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = fontSize.toString(),
        color = if (fontSize > curMaxSize) Color.LightGray else Color.Black
    )
}

private fun getFontSizes(model: BoardTextItemModel, viewModel: BoardItemViewModel) = when (model) {
    is StickyModel -> viewModel.stickyFontSizes
    is ShapeModel -> viewModel.shapeFontSizes
    is TextModel -> viewModel.textFontSizes
}