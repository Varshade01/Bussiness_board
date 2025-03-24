package com.rdua.whiteboard.board.model

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.constants.BlockingPadding
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.constants.DefaultSizes
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.exceptions.InvalidValueModelException

data class ShapeModel(
    override val id: String,
    override val coordinate: DpOffset,
    override val size: DpSize = DefaultSizes.shapeSize,
    override val isSelected: Boolean = false, // Shouldn't be saved persistently.
    override val isEditMode: Boolean = false, // Shouldn't be saved persistently.
    override val isLocked: Boolean = false,
    override val isBlockedBy: UserInfo? = null,
    override val blockingToken: BlockingToken? = null,
    override val color: Color = DefaultColors.shapeColor,
    override val text: String = "",
    override val textStyle: TextStyle = DefaultTextStyles.shapeUITextStyle,
    override val verticalTextAlignment: Alignment = DefaultTextStyles.shapeUITextAlignment,
    override val maxFontSize: TextUnit = DefaultTextStyles.absoluteMaxFontSize, // Shouldn't be saved persistently.
    override val isAutoFontSizeMode: Boolean = false,
    override val creator: String,
    override val createdAt: Timestamp,
    override val modifiedBy: String,
    override val modifiedAt: Timestamp,
    override val outerFrameId: String? = null,
    override val borderColor: Color = DefaultColors.shapeBorderColor,
    override val blockingPadding: BlockingPadding = BlockingPadding(all = 4.dp), // Shouldn't be saved persistently.
    val type: ShapeType,
) : BoardItemModel, BoardTextItemModel, BoardTextItemAutoFontModel, BoardColorItemModel, BorderableModel, BoardItemInnerModel {

    override fun toCopy(vararg values: ValueModel): ShapeModel {
        var item: ShapeModel = this.copy()

        values.forEach {
            item = when (it) {
                is ValueModel.Id -> item.copy(id = it.id)
                is ValueModel.Offset -> item.copy(coordinate = it.coordinate)
                is ValueModel.Size -> item.copy(size = it.size)
                is ValueModel.Selected -> item.copy(isSelected = it.isSelected)
                is ValueModel.Edit -> item.copy(isEditMode = it.isEditMode)
                is ValueModel.Lock -> item.copy(isLocked = it.isLocked)
                is ValueModel.BlockedBy -> item.copy(isBlockedBy = it.isBlockedBy)
                is ValueModel.BlockToken -> item.copy(blockingToken = it.blockingToken)
                is ValueModel.BackgroundColor -> item.copy(color = it.color)
                is ValueModel.Text -> item.copy(text = it.text)
                is ValueModel.TextStyle -> item.copy(textStyle = it.textStyle)
                is ValueModel.VerticalTextAlignment -> item.copy(verticalTextAlignment = it.alignment)
                is ValueModel.MaxFontSize -> item.copy(maxFontSize = it.maxFontSize)
                is ValueModel.AutoFontSize -> item.copy(isAutoFontSizeMode = it.isAutoFontSizeOn)
                is ValueModel.Type -> item.copy(type = it.type)
                is ValueModel.BorderColor -> item.copy(borderColor = it.color)
                is ValueModel.Creator -> item.copy(creator = it.creator)
                is ValueModel.CreatedAt -> item.copy(createdAt = it.timestamp)
                is ValueModel.ModifiedBy -> item.copy(modifiedBy = it.modifiedBy)
                is ValueModel.ModifiedAt -> item.copy(modifiedAt = it.timestamp)
                is ValueModel.OuterFrameId -> item.copy(outerFrameId = it.outerFrameId)

                is ValueModel.Rotation,
                is ValueModel.AutoTextWidth,
                is ValueModel.Author,
                is ValueModel.Scale,
                -> throw InvalidValueModelException()
            }
        }
        return item
    }
}
