package com.rdua.whiteboard.repository.boards

import com.rdua.whiteboard.invite_via_email.state.AddUserToBoardState
import com.rdua.whiteboard.board_options_dialog.state.DeleteBoardState
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import com.rdua.whiteboard.data.entities.boards.FirebaseBoardEntity
import com.rdua.whiteboard.data.entities.boards.provider.UpdateBoardStateData
import com.rdua.whiteboard.di.DefaultCoroutineScope
import com.rdua.whiteboard.firebase.database.boards.IFirebaseBoardsDataSource
import com.rdua.whiteboard.firebase.state.SaveBoardState
import com.rdua.whiteboard.firebase.state.UpdateBoardState
import com.rdua.whiteboard.home.state.BoardState
import com.rdua.whiteboard.rename_board.state.RenameBoardState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BoardsRepositoryImpl @Inject constructor(
    private val firebaseBoardsDataSource: IFirebaseBoardsDataSource,
    @DefaultCoroutineScope private val defaultCoroutineScope: CoroutineScope,
) : BoardsRepository {

    private val _boardsStateFlow: MutableStateFlow<List<FirebaseBoardEntity>?> = MutableStateFlow(null)
    private var isSubscribedToBoards = false
    private var boardsJob: Job? = null

    private val _boardEntityState: MutableStateFlow<FirebaseBoardEntity?> = MutableStateFlow(null)
    private var isSubscribedToBoardItem = false
    private var boardItemJob: Job? = null

    override suspend fun createBoard(boardEntity: FirebaseBoardEntity): BoardState =
        firebaseBoardsDataSource.createBoard(boardEntity)

    override fun clearBoardsCache() {
        isSubscribedToBoards = false
        boardsJob?.cancel()
        boardsJob = null
        _boardsStateFlow.tryEmit(null)
        firebaseBoardsDataSource.clearBoardsCache()
    }

    override suspend fun deleteBoard(id: String): DeleteBoardState =
        firebaseBoardsDataSource.deleteBoard(id)

    @Synchronized
    override fun subscribeToBoard(id: String?): StateFlow<FirebaseBoardEntity?> {
        if (!isSubscribedToBoardItem) {
            isSubscribedToBoardItem = true

            boardItemJob = defaultCoroutineScope.launch {
                _boardsStateFlow.collect { boards ->
                    val boardEntity: FirebaseBoardEntity? = boards?.find { it.id == id }
                    _boardEntityState.tryEmit(boardEntity)
                }
            }
        }
        return _boardEntityState
    }

    override suspend fun addUserToBoard(boardId: String, accessInfo: AccessInfo): AddUserToBoardState {
        return firebaseBoardsDataSource.addUserToBoard(boardId, accessInfo)
    }

    override fun clearBoardItem() {
        isSubscribedToBoardItem = false
        boardItemJob?.cancel()
        boardItemJob = null
    }

    @Synchronized
    override fun getBoards(userId: String): StateFlow<List<FirebaseBoardEntity>?> {
        if (!isSubscribedToBoards) {
            isSubscribedToBoards = true

            boardsJob = defaultCoroutineScope.launch {
                firebaseBoardsDataSource.getBoards(userId).collect { list ->
                    list?.let { emitBoards(it) }
                }
            }
        }
        return _boardsStateFlow
    }

    override suspend fun updateBoardState(data: UpdateBoardStateData): UpdateBoardState {
        return firebaseBoardsDataSource.updateBoardState(data)
    }

    override suspend fun saveBoardState(data: UpdateBoardStateData): SaveBoardState {
        return firebaseBoardsDataSource.saveBoardState(data)
    }

    private fun emitBoards(newBoards: List<FirebaseBoardEntity>) {
        val currentBoards = _boardsStateFlow.value?.toMutableList() ?: mutableListOf()

        removeBoards(currentBoards, newBoards)
        addAndUpdateBoards(currentBoards, newBoards)

        _boardsStateFlow.tryEmit(currentBoards)
    }

    private fun removeBoards(
        currentBoards: MutableList<FirebaseBoardEntity>,
        newBoards: List<FirebaseBoardEntity>,
    ) {
        val removeBoards: MutableList<FirebaseBoardEntity> = mutableListOf()

        currentBoards.forEach { currentBoard ->
            val board = newBoards.find { it.id == currentBoard.id }
            if (board == null) {
                removeBoards.add(currentBoard)
            }
        }
        removeBoards.forEach { currentBoards.remove(it) }
    }

    private fun addAndUpdateBoards(
        currentBoards: MutableList<FirebaseBoardEntity>,
        newBoards: List<FirebaseBoardEntity>,
    ) {
        newBoards.forEach { newBoard ->
            val index = currentBoards.indexOfFirst { it.id == newBoard.id }

            if (index != -1) {
                currentBoards.removeAt(index)
                currentBoards.add(index, newBoard)
            } else {
                currentBoards.add(newBoard)
            }
        }
    }

    override suspend fun renameBoard(boardEntity: FirebaseBoardEntity, newName: String): RenameBoardState {
        return firebaseBoardsDataSource.renameBoard(boardEntity, newName)
    }
}
