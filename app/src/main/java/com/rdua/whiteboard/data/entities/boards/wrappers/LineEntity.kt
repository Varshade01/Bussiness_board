package com.rdua.whiteboard.data.entities.boards.wrappers

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.model.LineModel

@AutoMap("Line")
data class LineEntity(
    val id: String,
    @AutoMap val size: DpSizeEntity,
    @AutoMap val coordinate: DpOffsetEntity,
    @AutoMap val endCoordinate: DpOffsetEntity,
    val rotationAngle: Float,
    val isLocked: Boolean?,
    @AutoMap val blockingToken: BlockingTokenEntity?,
    val creator: String,
    val createdAt: String,
    val modifiedBy: String,
    val modifiedAt: String,
    val color: String,
    val outerFrameId: String?,
    val zPosition: Int,
)

fun LineModel.toEntity(position: Int) = LineEntity(
    id = id,
    size = size.toDpSizeEntity(),
    coordinate = coordinate.toDpOffsetEntity(),
    endCoordinate = endCoordinate.toDpOffsetEntity(),
    rotationAngle = rotationAngle,
    isLocked = isLocked,
    blockingToken = blockingToken?.toEntity(),
    creator = creator,
    createdAt = createdAt.toJson(),
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.toJson(),
    color = color.toColorJson(),
    outerFrameId = outerFrameId,
    zPosition = position,
)

fun LineEntity.toModel() = LineModel(
    id = id,
    size = size.toDpSize(),
    coordinate = coordinate.toDpOffset(),
    endCoordinate = endCoordinate.toDpOffset(),
    rotationAngle = rotationAngle,
    isLocked = isLocked ?: false,
    blockingToken = blockingToken?.toBlockingToken(),
    creator = creator,
    createdAt = createdAt.fromTimestampJson()!!,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.fromTimestampJson()!!,
    color = color.fromColorJson() ?: DefaultColors.lineColor,
    outerFrameId = outerFrameId,
)
