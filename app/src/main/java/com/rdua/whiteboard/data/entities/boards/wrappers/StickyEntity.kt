package com.rdua.whiteboard.data.entities.boards.wrappers

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.model.StickyModel

@AutoMap("Sticky")
data class StickyEntity(
    val id: String,
    @AutoMap val coordinate: DpOffsetEntity,
    @AutoMap val size: DpSizeEntity,
    val scale: Float?,
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
    val zPosition: Int,
    val outerFrameId: String?,
)

fun StickyModel.toEntity(position: Int) = StickyEntity(
    id = id,
    coordinate = coordinate.toDpOffsetEntity(),
    size = size.toDpSizeEntity(),
    scale = scale,
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
    zPosition = position,
    outerFrameId = outerFrameId,
)

// Manually provide default values in case they're not found in Firebase database.
/**
 * Converts serialized [StickyEntity] to [StickyModel].
 *
 * @param resolveStickyAuthor the function to provide `String` name of the StickyNote author by their id.
 */
suspend fun StickyEntity.toModel(
    resolveStickyAuthor: suspend (id: String) -> String? = { null },
) = StickyModel(
    id = id,
    coordinate = coordinate.toDpOffset(),
    size = size.toDpSize(),
    scale = scale ?: 1f,
    color = color.fromColorJson() ?: DefaultColors.stickyColor,
    text = text,
    textStyle = textStyle?.toTextStyle() ?: DefaultTextStyles.stickyUITextStyle,
    verticalTextAlignment = verticalTextAlignment?.toVerticalTextAlignment()
        ?: DefaultTextStyles.stickyUITextAlignment,
    isAutoFontSizeMode = isAutoFontSizeMode,
    isLocked = isLocked ?: false,
    blockingToken = blockingToken?.toBlockingToken(),
    creator = creator,
    createdAt = createdAt.fromTimestampJson()!!,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt.fromTimestampJson()!!,
    author = resolveStickyAuthor(creator),
    outerFrameId = outerFrameId,
)
