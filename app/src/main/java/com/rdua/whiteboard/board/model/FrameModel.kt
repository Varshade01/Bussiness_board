package com.rdua.whiteboard.board.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.rdua.whiteboard.board.constants.BlockingPadding
import com.rdua.whiteboard.board.constants.DefaultSizes
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.exceptions.InvalidValueModelException

data class FrameModel(
    override val id: String,
    override val size: DpSize = DefaultSizes.frameSize,
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
    override val color: Color,
    override val borderColor: Color,
    override val blockingPadding: BlockingPadding = BlockingPadding(),
) : BoardItemModel, BoardColorItemModel, BorderableModel {

    override fun toCopy(vararg values: ValueModel): FrameModel {
        var item: FrameModel = this.copy()

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
                is ValueModel.Creator -> item.copy(creator = it.creator)
                is ValueModel.CreatedAt -> item.copy(createdAt = it.timestamp)
                is ValueModel.ModifiedBy -> item.copy(modifiedBy = it.modifiedBy)
                is ValueModel.ModifiedAt -> item.copy(modifiedAt = it.timestamp)
                is ValueModel.BackgroundColor -> item.copy(color = it.color)
                is ValueModel.BorderColor -> item.copy(borderColor = it.color)


                is ValueModel.Rotation,
                is ValueModel.OuterFrameId,
                is ValueModel.VerticalTextAlignment,
                is ValueModel.TextStyle,
                is ValueModel.MaxFontSize,
                is ValueModel.Author,
                is ValueModel.Text,
                is ValueModel.Type,
                is ValueModel.AutoTextWidth,
                is ValueModel.AutoFontSize,
                is ValueModel.Scale,
                -> throw InvalidValueModelException()
            }
        }
        return item
    }
}
