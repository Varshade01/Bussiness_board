package com.rdua.whiteboard.data.entities.boards.wrappers

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.model.ShapeModel

@AutoMap("Shape")
data class ShapeEntity(
    val id: String,
    @AutoMap val coordinate: DpOffsetEntity,
    @AutoMap val size: DpSizeEntity,
    val color: String,
    val text: String,
    @AutoMap val textStyle: TextStyleEntity?,
    @AutoMap val verticalTextAlignment: VerticalTextAlignmentEntity?,
    val isAutoFontSizeMode: Boolean,
    val isLocked: Boolean?,
    @AutoMap val blockingToken: BlockingTokenEntity?,
    val creator: String,
    val createdAt: String,
    val modifiedBy: String,
    val modifiedAt: String,
    val type: String, // AutoMap doesn't support enums for now. Convert manually.
    val borderColor: String?,
    val zPosition: Int,
    val outerFrameId: String?,
)

fun ShapeModel.toEntity(position: Int) = ShapeEntity(
    id = id,
    coordinate = coordinate.toDpOffsetEntity(),
    size = size.toDpSizeEntity(),
    color = color.toColorJson(),
    text = text,
    textStyle = textStyle.toTextStyleEntity(),
    verticalTextAlignment = verticalTextAlignment.toVerticalTextAlignmentEntity(),
    isAutoFontSizeMode = isAutoFontSizeMode,
    isLocked = isLocked,
    blockingToken = blockingToken?.toEntity(),
    creator = creator,
    createdAt = createdAt.toJson(),
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.toJson(),
    type = type.toJson(),
    borderColor = borderColor.toColorJson(),
    zPosition = position,
    outerFrameId = outerFrameId,
)

fun ShapeEntity.toModel() = ShapeModel(
    id = id,
    coordinate = coordinate.toDpOffset(),
    size = size.toDpSize(),
    color = color.fromColorJson() ?: DefaultColors.shapeColor,
    text = text,
    textStyle = textStyle?.toTextStyle() ?: DefaultTextStyles.shapeUITextStyle,
    verticalTextAlignment = verticalTextAlignment?.toVerticalTextAlignment()
        ?: DefaultTextStyles.shapeUITextAlignment,
    isAutoFontSizeMode = isAutoFontSizeMode,
    isLocked = isLocked ?: false,
    blockingToken = blockingToken?.toBlockingToken(),
    creator = creator,
    createdAt = createdAt.fromTimestampJson()!!,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.fromTimestampJson()!!,
    type = type.fromShapeTypeJson(),
    borderColor = borderColor?.fromColorJson() ?: DefaultColors.shapeBorderColor,
    outerFrameId = outerFrameId,
)