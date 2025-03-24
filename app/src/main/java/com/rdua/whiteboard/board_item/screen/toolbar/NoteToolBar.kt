package com.rdua.whiteboard.board_item.screen.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.model.BoardColorItemModel
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.BorderableModel
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.board.model.LineModel
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.board.utils.conditional
import com.rdua.whiteboard.board.utils.consumeAllPointerEvents
import com.rdua.whiteboard.board_item.event.BoardToolBarEvent
import com.rdua.whiteboard.board_item.event.TextToolbarEvent
import com.rdua.whiteboard.board_item.event.ThreeDotsEvent
import com.rdua.whiteboard.board_item.screen.toolbar.color_bar.ColorOption
import com.rdua.whiteboard.board_item.viewmodel.BoardItemViewModel
import com.rdua.whiteboard.common.composable.ColorButton
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun NoteToolBar(
    model: BoardItemModel,
    viewModel: BoardItemViewModel,
    consumeClicks: Boolean = true,
) {
    val optionsList: List<NoteToolBarOptions> =
        if (model.isLocked) {
            lockOptions
        } else {
            when (model) {
                is ShapeModel -> {
                    if (model.text.isEmpty()) {
                        shapeOptionsWithoutText
                    } else {
                        shapeOptions
                    }
                }

                is StickyModel -> stickyOptions
                is TextModel -> textOptions
                is FrameModel -> frameOptions
                is LineModel -> lineOptions
            }
        }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(bottom = MaterialTheme.spaces.space3)
            .conditional(condition = consumeClicks, ifTrue = { consumeAllPointerEvents() })
            .shadow(MaterialTheme.spaces.space2, RoundedCornerShape(MaterialTheme.spaces.space3))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(MaterialTheme.spaces.space3)
            ),

        ) {
        LazyRow(
            modifier = Modifier
                .wrapContentSize()
                .padding(all = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.spaces.space4,
                alignment = Alignment.CenterHorizontally
            )
        ) {

            items(optionsList) { item ->
                NoteToolBarItem(item = item, viewModel = viewModel, model = model)
            }
        }
    }
}

@Composable
private fun NoteToolBarItem(
    item: NoteToolBarOptions,
    viewModel: BoardItemViewModel,
    model: BoardItemModel,
) {
    when (item) {

        is NoteToolBarOptions.Delete -> {
            NoteToolBarItemIcon(
                enabled = !model.isLocked,
                painter = painterResource(id = R.drawable.ic_toolbar_note_delete),
                contentDescription = stringResource(R.string.note_toolbar_icon_delete_description),
                modifier = Modifier.clickable {
                    viewModel.onEvent(BoardToolBarEvent.DeleteBoardItem)
                },
            )
        }

        is NoteToolBarOptions.Border -> {
            ColorButton(
                enabled = !model.isLocked,
                colorOption = ColorOption.BorderColor,
                color = (model as? BorderableModel)?.borderColor ?: Color.Black,
                onClick = { viewModel.onEvent(BoardToolBarEvent.OpenBorderColorBar) },
            )
        }

        is NoteToolBarOptions.Color -> {
            val color: Color? = (model as? BoardColorItemModel)?.color

            if (color != null) {
                ColorButton(
                    enabled = !model.isLocked,
                    colorOption = ColorOption.BackgroundColor,
                    color = color,
                    onClick = { viewModel.onEvent(BoardToolBarEvent.OpenBackgroundColorBar) },
                )
            }
        }

        is NoteToolBarOptions.Copy -> {
            NoteToolBarItemIcon(
                painter = painterResource(id = R.drawable.ic_toolbar_note_copy),
                contentDescription = stringResource(R.string.note_toolbar_icon_copy_description),
                modifier = Modifier.clickable {
                    viewModel.onEvent(BoardToolBarEvent.CopyBoardItem)
                },
            )
        }

        is NoteToolBarOptions.Others -> {
            NoteToolBarItemIcon(
                painter = painterResource(id = R.drawable.ic_toolbar_sticky_others),
                contentDescription = stringResource(R.string.note_toolbar_icon_others_description),
                modifier = Modifier.clickable {
                    viewModel.onEvent(ThreeDotsEvent.OpenToolbar)
                },
            )
        }

        is NoteToolBarOptions.Text -> {
            NoteToolBarItemIcon(
                enabled = !model.isLocked,
                painter = painterResource(id = R.drawable.ic_toolbar_note_text),
                contentDescription = stringResource(R.string.note_toolbar_icon_text_description),
                modifier = Modifier.clickable(enabled = true) {
                    viewModel.onEvent(TextToolbarEvent.ToggleTextToolbar)
                },
            )
        }

        is NoteToolBarOptions.Divider -> {
            NoteToolBarItemIcon(
                enabled = !model.isLocked,
                painter = painterResource(id = R.drawable.ic_toolbar_note_divider),
                contentDescription = stringResource(R.string.note_toolbar_icon_divider_description),
            )
        }

        NoteToolBarOptions.Lock -> {
            NoteToolBarItemIcon(
                enabled = model.isLocked,
                painter = painterResource(id = R.drawable.ic_close_lock),
                contentDescription = stringResource(R.string.three_dots_lock),
                modifier = Modifier.clickable {
                    viewModel.onEvent(ThreeDotsEvent.Unlock(model.id))
                },
            )
        }
    }
}

@Composable
private fun NoteToolBarItemIcon(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    painter: Painter,
    contentDescription: String,
) {
    if (enabled) {
        Icon(
            modifier = modifier.size(size = MaterialTheme.spaces.space6),
            painter = painter,
            contentDescription = contentDescription,
        )
    }
}
