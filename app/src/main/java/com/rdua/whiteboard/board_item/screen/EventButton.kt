package com.rdua.whiteboard.board_item.screen

import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_item.event.BoardItemEvent

sealed class EventButton(
    val iconId: Int,
    val titleId: Int,
    val event: BoardItemEvent?,
) {
    object StickyButton : EventButton(
        iconId = R.drawable.ic_sticky,
        titleId = R.string.sticky,
        event = BoardItemEvent.StickyEvent
    )

    object TextButton : EventButton(
        iconId = R.drawable.ic_text,
        titleId = R.string.text,
        event = BoardItemEvent.TextEvent
    )

    object ShapesButton : EventButton(
        iconId = R.drawable.ic_shape,
        titleId = R.string.shapes,
        event = BoardItemEvent.ShapesEvent
    )

    object ImageButton : EventButton(
        iconId = R.drawable.ic_image,
        titleId = R.string.image,
        event = BoardItemEvent.ImageEvent
    )

    object CreateFrameButton : EventButton(
        iconId = R.drawable.ic_frame,
        titleId = R.string.create_frame,
        event = BoardItemEvent.CreateFrameEvent
    )

    object AddCommentButton : EventButton(
        iconId = R.drawable.ic_comment,
        titleId = R.string.add_comment,
        event = BoardItemEvent.AddCommentEvent
    )

    object MoreButton : EventButton(
        iconId = R.drawable.ic_more,
        titleId = R.string.more,
        event = null
    )

    object UndoButton : EventButton(
        iconId = R.drawable.ic_undo,
        titleId = R.string.undo,
        event = BoardItemEvent.UndoAction
    )

    object RedoButton : EventButton(
        iconId = R.drawable.ic_redo,
        titleId = R.string.redo,
        event = BoardItemEvent.RedoAction
    )
}
