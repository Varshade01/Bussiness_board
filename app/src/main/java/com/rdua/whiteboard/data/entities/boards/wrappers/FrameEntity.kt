package com.rdua.whiteboard.data.entities.boards.wrappers

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.common.time.Timestamp

@AutoMap("Frame")
data class FrameEntity(
    val id: String,
    @AutoMap val coordinate: DpOffsetEntity,
    @AutoMap val size: DpSizeEntity,
    val color: String,
    val borderColor: String?,
    val isLocked: Boolean,
    @AutoMap val blockingToken: BlockingTokenEntity?,
    val creator: String,
    val createdAt: String,
    val modifiedBy: String,
    val modifiedAt: String,
    val zPosition: Int,
)

fun FrameModel.toEntity(position: Int) = FrameEntity(
    id = id,
    coordinate = coordinate.toDpOffsetEntity(),
    size = size.toDpSizeEntity(),
    color = color.toColorJson(),
    borderColor = borderColor.toColorJson(),
    isLocked = isLocked,
    blockingToken = blockingToken?.toEntity(),
    creator = creator,
    createdAt = createdAt.toJson(),
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.toJson(),
    zPosition = position,
)

fun FrameEntity.toModel() = FrameModel(
    id = id,
    coordinate = coordinate.toDpOffset(),
    size = size.toDpSize(),
    isLocked = isLocked,
    blockingToken = blockingToken?.toBlockingToken(),
    creator = creator,
    createdAt = createdAt.fromTimestampJson() ?: Timestamp.now(),
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.fromTimestampJson() ?: Timestamp.now(),
    color = color.fromColorJson() ?: DefaultColors.frameColor,
    borderColor = borderColor?.fromColorJson() ?: DefaultColors.frameBorderColor,
)
