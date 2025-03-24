package com.rdua.whiteboard.board_item.event

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import com.rdua.whiteboard.board.composable.utils.LineData

sealed interface BoardItemEvent {

    data class SearchTextChangeEvent(val searchText: String) : BoardItemEvent
    object NavigateBackEvent : BoardItemEvent
    object ShareEvent : BoardItemEvent
    object MicrophoneEvent : BoardItemEvent
    object StickyEvent : BoardItemEvent
    object TextEvent : BoardItemEvent
    object ShapesEvent : BoardItemEvent
    object ImageEvent : BoardItemEvent
    object CreateFrameEvent : BoardItemEvent
    object AddCommentEvent : BoardItemEvent
    object CloseBottomSheetShape : BoardItemEvent
    class Select(val id: String) : BoardItemEvent
    class EnableEditMode(val id: String) : BoardItemEvent
    class DisableEditMode(val id: String) : BoardItemEvent
    class DisableAutoTextWidth(val id: String) : BoardItemEvent
    class PositionChange(val id: String, val offset: DpOffset) : BoardItemEvent
    class TextChange(val id: String, val newText: String) : BoardItemEvent
    class SizeChange(val id: String, val size: DpSize, val offset: DpOffset) : BoardItemEvent
    class TextSizeChange(val id: String, val size: DpSize) : BoardItemEvent
    class EndResize(val id: String) : BoardItemEvent
    class EndChangePosition(val id: String) : BoardItemEvent
    class ScaleChange(val id: String, val scale: Float, val offset: DpOffset) : BoardItemEvent
    class TextScaleChange(val id: String, val newFont: TextUnit, val offset: DpOffset) : BoardItemEvent
    class FontSizeChange(val id: String, val newFont: TextUnit) : BoardItemEvent
    class AutoFontSizeModeChange(val id: String, val isEnabled: Boolean) : BoardItemEvent
    class SetMaxFontSize(val id: String, val maxSize: TextUnit) : BoardItemEvent
    class ResizeLine(val lineData: LineData?) : BoardItemEvent

    object UndoAction : BoardItemEvent
    object RedoAction : BoardItemEvent

    object DismissSelectBoardItem : BoardItemEvent
}
