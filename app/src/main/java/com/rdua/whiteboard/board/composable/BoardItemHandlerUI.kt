package com.rdua.whiteboard.board.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import com.rdua.whiteboard.board.draw_shape.onDrawShape
import com.rdua.whiteboard.board.composable.utils.LineData
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.board.model.LineModel
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.board.model.TextModel

@Composable
fun BoardItemHandlerUI(
    model: BoardItemModel,
    onSelect: (id: String) -> Unit = { },
    onEnableEditMode: (id: String) -> Unit = { },
    onDisableEditMode: (id: String) -> Unit = { },
    onMove: (id: String, offset: DpOffset) -> Unit = { _, _ -> },
    onDragEnd: (id: String) -> Unit = { },
    onTextChange: (id: String, newText: String) -> Unit = { _, _ -> },
    onSizeChanged: (id: String, newSize: DpSize, offset: DpOffset) -> Unit = { _, _, _ -> },
    onResizeEnd: (id: String) -> Unit = { },
    onScale: (id: String, scale: Float, offset: DpOffset) -> Unit = { _, _, _ -> },
    onTextScale: (id: String, newFontSize: TextUnit, offset: DpOffset) -> Unit = { _, _, _ -> },
    onDisableAutoTextWidth: (id: String) -> Unit = { },
    onFontSizeChange: (id: String, newFont: TextUnit) -> Unit = { _, _ -> },
    onSetMaxFontSize: (id: String, maxFont: TextUnit) -> Unit = { _, _ -> },
    onAutoFontSizeModeChange: (id: String, isEnabled: Boolean) -> Unit = { _, _ -> },
    onTextSizeChanged: (id: String, newSize: DpSize) -> Unit = { _, _ -> },
    onResizeLine: (lineData: LineData?) -> Unit = { },
) {
    when (model) {
        is StickyModel -> {
            StickyNoteUI(
                data = model,
                onSelect = onSelect,
                onEnableEditMode = onEnableEditMode,
                onDisableEditMode = onDisableEditMode,
                onTextChange = onTextChange,
                onMove = onMove,
                onScale = onScale,
                onResizeEnd = onResizeEnd,
                onDragEnd = onDragEnd,
                onFontSizeChange = onFontSizeChange,
                onSetMaxFontSize = onSetMaxFontSize,
                onAutoFontSizeModeChange = onAutoFontSizeModeChange,
            )
        }

        is ShapeModel -> {
            ShapeUI(
                data = model,
                onSelect = onSelect,
                onEnableEditMode = onEnableEditMode,
                onDisableEditMode = onDisableEditMode,
                onTextChange = onTextChange,
                onMove = onMove,
                onSizeChange = onSizeChanged,
                onDragEnd = onDragEnd,
                onResizeEnd = onResizeEnd,
                drawShape = { onDrawShape(model = model) },
                onFontSizeChange = onFontSizeChange,
                onSetMaxFontSize = onSetMaxFontSize,
                onAutoFontSizeModeChange = onAutoFontSizeModeChange,
            )
        }

        is TextModel -> {
            TextUI(
                data = model,
                onSelect = onSelect,
                onEnableEditMode = onEnableEditMode,
                onDisableEditMode = onDisableEditMode,
                onTextChange = onTextChange,
                onMove = onMove,
                onSizeChange = onSizeChanged,
                onTextScaleChange = onTextScale,
                onTextSizeChange = onTextSizeChanged,
                onDisableAutoTextWidth = onDisableAutoTextWidth,
                onResizeEnd = onResizeEnd,
                onDragEnd = onDragEnd
            )
        }

        is FrameModel -> {
            FrameUI(
                data = model,
                onSelectChange = onSelect,
                onEnableEditMode = onEnableEditMode,
                onMove = onMove,
                onDragEnd = onDragEnd,
            )
        }

        is LineModel -> {
            LineUI(
                data = model,
                onSelect = onSelect,
                onEnableEditMode = onEnableEditMode,
                onMove = onMove,
                onDragEnd = onDragEnd,
                onResizeLine = onResizeLine,
                onResizeEnd = onResizeEnd,
            )
        }
    }
}
