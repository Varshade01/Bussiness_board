package com.rdua.whiteboard.firebase.database.boards

import com.rdua.whiteboard.invite_via_email.state.AddUserToBoardState
import com.rdua.whiteboard.board_options_dialog.state.DeleteBoardState
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import com.rdua.whiteboard.data.entities.boards.FirebaseBoardEntity
import com.rdua.whiteboard.data.entities.boards.provider.UpdateBoardStateData
import com.rdua.whiteboard.firebase.state.SaveBoardState
import com.rdua.whiteboard.firebase.state.UpdateBoardState
import com.rdua.whiteboard.home.state.BoardState
import com.rdua.whiteboard.rename_board.state.RenameBoardState
import kotlinx.coroutines.flow.StateFlow

interface IFirebaseBoardsDataSource {

    suspend fun createBoard(boardEntity: FirebaseBoardEntity): BoardState

    fun getBoards(userId: String): StateFlow<List<FirebaseBoardEntity>?>

    fun clearBoardsCache()

    suspend fun deleteBoard(id: String): DeleteBoardState

    suspend fun renameBoard(boardEntity: FirebaseBoardEntity, newName: String): RenameBoardState

    suspend fun addUserToBoard(boardId: String, accessInfo: AccessInfo): AddUserToBoardState

    suspend fun saveBoardState(
        data: UpdateBoardStateData,
    ): SaveBoardState

    suspend fun updateBoardState(
        data: UpdateBoardStateData,
    ): UpdateBoardState
}
