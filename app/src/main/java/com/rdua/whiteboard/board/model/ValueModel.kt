package com.rdua.whiteboard.board.model

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.common.time.Timestamp

sealed interface ValueModel {

    data class Id(val id: String) : ValueModel
    data class Size(val size: DpSize) : ValueModel
    data class Offset(
        val coordinate: DpOffset,
        val endCoordinate: DpOffset = DpOffset.Zero,
    ) : ValueModel

    data class Rotation(val angle: Float) : ValueModel
    data class Scale(val scale: Float) : ValueModel
    data class Selected(val isSelected: Boolean) : ValueModel
    data class Edit(val isEditMode: Boolean) : ValueModel
    data class Lock(val isLocked: Boolean) : ValueModel
    data class BlockedBy(val isBlockedBy: UserInfo?) : ValueModel
    data class BlockToken(val blockingToken: BlockingToken?) : ValueModel

    data class Text(val text: String) : ValueModel
    data class TextStyle(val textStyle: androidx.compose.ui.text.TextStyle) : ValueModel
    data class VerticalTextAlignment(val alignment: Alignment) : ValueModel
    data class MaxFontSize(val maxFontSize: TextUnit) : ValueModel
    data class AutoFontSize(val isAutoFontSizeOn: Boolean) : ValueModel
    data class Author(val author: String) : ValueModel

    data class Type(val type: ShapeType) : ValueModel
    data class BorderColor(val color: Color) : ValueModel
    data class BackgroundColor(val color: Color) : ValueModel
    data class AutoTextWidth(val isAutoWidthOn: Boolean) : ValueModel

    data class Creator(val creator: String) : ValueModel
    data class CreatedAt(val timestamp: Timestamp) : ValueModel
    data class ModifiedBy(val modifiedBy: String) : ValueModel
    data class ModifiedAt(val timestamp: Timestamp) : ValueModel

    data class OuterFrameId(val outerFrameId: String?) : ValueModel
}
