package com.rdua.whiteboard.board.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.rdua.whiteboard.board.constants.BlockingPadding
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.constants.DefaultSizes
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.exceptions.InvalidValueModelException

data class LineModel(
    override val id: String,
    override val size: DpSize = DefaultSizes.lineSize,
    override val coordinate: DpOffset,
    override val isSelected: Boolean = false,
    override val isEditMode: Boolean = false,
    override val isLocked: Boolean = false,
    override val isBlockedBy: UserInfo? = null,
    override val blockingToken: BlockingToken? = null,
    override val creator: String,
    override val createdAt: Timestamp,
    override val modifiedBy: String,
    override val modifiedAt: Timestamp,
    override val blockingPadding: BlockingPadding = BlockingPadding(),
    override val color: Color = DefaultColors.lineColor,
    override val outerFrameId: String? = null,
    val endCoordinate: DpOffset,
    val rotationAngle: Float,
) : BoardItemModel, BoardColorItemModel, BoardItemInnerModel {

    override fun toCopy(vararg values: ValueModel): LineModel {
        var item: LineModel = this.copy()

        values.forEach {
            item = when (it) {
                is ValueModel.Id -> item.copy(id = it.id)
                is ValueModel.Offset ->
                    item.copy(coordinate = it.coordinate, endCoordinate = it.endCoordinate)

                is ValueModel.Rotation -> item.copy(rotationAngle = it.angle)
                is ValueModel.Size -> item.copy(size = it.size)
                is ValueModel.Selected -> item.copy(isSelected = it.isSelected)
                is ValueModel.Edit -> item.copy(isEditMode = it.isEditMode)
                is ValueModel.Lock -> item.copy(isLocked = it.isLocked)
                is ValueModel.BlockedBy -> item.copy(isBlockedBy = it.isBlockedBy)
                is ValueModel.BlockToken -> item.copy(blockingToken = it.blockingToken)
                is ValueModel.BackgroundColor -> item.copy(color = it.color)
                is ValueModel.Creator -> item.copy(creator = it.creator)
                is ValueModel.CreatedAt -> item.copy(createdAt = it.timestamp)
                is ValueModel.ModifiedBy -> item.copy(modifiedBy = it.modifiedBy)
                is ValueModel.ModifiedAt -> item.copy(modifiedAt = it.timestamp)
                is ValueModel.OuterFrameId -> item.copy(outerFrameId = it.outerFrameId)

                is ValueModel.BorderColor,
                is ValueModel.Text,
                is ValueModel.TextStyle,
                is ValueModel.Type,
                is ValueModel.VerticalTextAlignment,
                is ValueModel.MaxFontSize,
                is ValueModel.AutoTextWidth,
                is ValueModel.AutoFontSize,
                is ValueModel.Author,
                is ValueModel.Scale,
                -> throw InvalidValueModelException()
            }
        }
        return item
    }
}
