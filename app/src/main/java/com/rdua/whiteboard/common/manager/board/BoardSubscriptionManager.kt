package com.rdua.whiteboard.common.manager.board

import com.rdua.whiteboard.board_item.usecase.IClearBoardItemUseCase
import com.rdua.whiteboard.board_item.usecase.ISubscribeToBoardUseCase
import com.rdua.whiteboard.data.entities.boards.BoardEntityState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides a centralized system to manage board subscription in different part of application.
 *
 * Intended as a way for a ViewModel to manually stop/replace subscriptions of a different ViewModel
 * without waiting for it to unsubscribe.
 */
@Singleton
class BoardSubscriptionManager @Inject constructor(
    private val subscribeToBoardUseCase: ISubscribeToBoardUseCase,
    private val clearBoardItemUseCase: IClearBoardItemUseCase,
) {
    private var subscribedToBoardId: String? = null

    /**
     * Subscribes to [boardId]. If [BoardSubscriptionManager] was subscribed to a board, unsubscribes
     * from it.
     */
    fun subscribeTo(boardId: String?): StateFlow<BoardEntityState?> {
        if (boardId != subscribedToBoardId) {
            clearBoardItemUseCase.clearBoardItem()
            subscribedToBoardId = boardId
        }
        return subscribeToBoardUseCase.subscribeToBoard(boardId)
    }

    /**
     * If [boardId] matches currently subscribed board, unsubscribes from it.
     */
    fun unsubscribeFrom(boardId: String?) {
        if (boardId == subscribedToBoardId) {
            clearBoardItemUseCase.clearBoardItem()
            subscribedToBoardId = null
        }
    }

    /**
     * If [BoardSubscriptionManager] is subscribed to a board, unsubscribes from it.
     */
    fun unsubscribeFromCurrent() {
        if (subscribedToBoardId != null) {
            clearBoardItemUseCase.clearBoardItem()
            subscribedToBoardId = null
        }
    }
}