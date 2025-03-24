package com.rdua.whiteboard.data.entities.boards.provider

import auto_map.internal.toMap
import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.data.entities.boards.BoardMapper
import javax.inject.Inject

/**
 * Provides data structures to update board table in Firebase database.
 */
class UpdateBoardDataProvider @Inject constructor(
    private val boardMapper: BoardMapper,
) {
    /**
     * Provides map structure representing [BoardItemModel] structure in board database.
     *
     * Data structure (Map<String, Any?>) contains:
     * "content/${item.id}" to Map<String, Any?>
     */
    fun createSaveBoardItemData(
        boardId: String,
        item: BoardItemModel,
        itemPosition: Int,
    ): UpdateBoardStateData {
        val firebaseItem = boardMapper.toFirebaseBoardItem(item, itemPosition)

        val data = mapOf("content/${item.id}" to firebaseItem)
        return UpdateBoardStateData(path = boardId, data = data)
    }

    /**
     * Provides map structure representing [BoardItemModel] and other board fields that have to update
     * when updating item.
     *
     * Data structure (Map<String, Any?>) contains:
     * "modifiedBy" to modifiedBy,
     * "modifiedAt" to modifiedAt,
     * "content/${item.id}" to Map<String, Any?>
     */
    fun createSaveBoardItemData(
        boardId: String,
        item: BoardItemModel,
        itemPosition: Int,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ): UpdateBoardStateData {
        val firebaseItem = boardMapper.toFirebaseBoardItem(item, itemPosition)
        val modifyData = ModifyData(modifiedBy = modifiedBy, modifiedAt = modifiedAt)

        val data = modifyData.toMap() + mapOf("content/${item.id}" to firebaseItem)
        return UpdateBoardStateData(path = boardId, data = data)
    }

    /**
     * Provides map structure representing board content to update.
     *
     * Data structure (Map<String, Any?>) contains:
     * "modifiedBy" to modifiedBy,
     * "modifiedAt" to modifiedAt,
     * "content" to Map<String, Any?>
     *   "item1_id" to Map<String, Any?>
     *   "item2_id" to Map<String, Any?>
     *  ...
     */
    fun createSaveBoardStateData(
        boardId: String,
        boardContent: List<BoardItemModel>,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ): UpdateBoardStateData {
        val firebaseContent = boardMapper.toFirebaseBoardContent(boardContent)
        val modifyData = ModifyData(modifiedBy = modifiedBy, modifiedAt = modifiedAt)

        val data = modifyData.toMap() + mapOf("content" to firebaseContent)
        return UpdateBoardStateData(path = boardId, data = data)
    }

    /**
     * Provides map structure for deleting a board item.
     *
     * Data structure (Map<String, Any?>) contains:
     * "modifiedBy" to modifiedBy,
     * "modifiedAt" to modifiedAt,
     * "content/$itemId" to null
     */
    fun createDeleteBoardItemData(
        boardId: String,
        itemId: String,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ): UpdateBoardStateData {
        val modifyData = ModifyData(modifiedBy = modifiedBy, modifiedAt = modifiedAt)

        val data = modifyData.toMap() + mapOf("content/$itemId" to null)
        return UpdateBoardStateData(path = boardId, data = data)
    }
}