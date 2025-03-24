package com.rdua.whiteboard.data.entities.boards.wrappers

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.model.TextModel

@AutoMap("Text")
data class TextEntity(
    val id: String,
    @AutoMap val coordinate: DpOffsetEntity,
    @AutoMap val size: DpSizeEntity,
    val text: String,
    @AutoMap val textStyle: TextStyleEntity?,
    val isLocked: Boolean?,
    @AutoMap val blockingToken: BlockingTokenEntity?,
    val creator: String,
    val createdAt: String,
    val modifiedBy: String,
    val modifiedAt: String,
    val isAutoWidthMode: Boolean,
    val zPosition: Int,
    val outerFrameId: String?,
)

fun TextModel.toEntity(position: Int) = TextEntity(
    id = id,
    coordinate = coordinate.toDpOffsetEntity(),
    size = size.toDpSizeEntity(),
    text = text,
    textStyle = textStyle.toTextStyleEntity(),
    isLocked = isLocked,
    blockingToken = blockingToken?.toEntity(),
    creator = creator,
    createdAt = createdAt.toJson(),
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.toJson(),
    isAutoWidthMode = isAutoWidthMode,
    zPosition = position,
    outerFrameId = outerFrameId,
)

fun TextEntity.toModel() = TextModel(
    id = id,
    coordinate = coordinate.toDpOffset(),
    size = size.toDpSize(),
    text = text,
    textStyle = textStyle?.toTextStyle() ?: DefaultTextStyles.textUITextStyle,
    isLocked = isLocked ?: false,
    blockingToken = blockingToken?.toBlockingToken(),
    creator = creator,
    createdAt = createdAt.fromTimestampJson()!!,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.fromTimestampJson()!!,
    isAutoWidthMode = isAutoWidthMode,
    outerFrameId = outerFrameId,
)
