package com.rdua.whiteboard.data.entities.boards

/**
 * Represents different states of [BoardEntity].
 *
 * [BoardEntityState.Loading] - should be the state of the [BoardEntityState] until the [BoardEntity] is
 * received from the source. [BoardEntity] should be `null` only when data is absent in the source.
 */
interface BoardEntityState {
    object Loading : BoardEntityState
}