package com.rdua.whiteboard.board_item.event

import androidx.compose.ui.graphics.Color

sealed interface BoardToolBarEvent {

    // toolbar board item border color
    object OpenBorderColorBar : BoardToolBarEvent
    data class SelectedBorderColor(val color: Color) : BoardToolBarEvent

    // toolbar board item background color
    object OpenBackgroundColorBar : BoardToolBarEvent
    data class SelectedBackgroundColor(val color: Color) : BoardToolBarEvent

    // toolbar board item options
    object CopyBoardItem : BoardToolBarEvent
    object DeleteBoardItem : BoardToolBarEvent
}
