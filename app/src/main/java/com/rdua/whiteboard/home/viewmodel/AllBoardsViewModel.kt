package com.rdua.whiteboard.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.usecases.GetDeviceIdUseCase
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import com.rdua.whiteboard.data.entities.boards.access.AccessLevel
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.firebase.auth.FirebaseAuth
import com.rdua.whiteboard.home.event.AllBoardsEvent
import com.rdua.whiteboard.home.navigation.IHomeNavigationActions
import com.rdua.whiteboard.home.state.AllBoardsUIState
import com.rdua.whiteboard.home.state.BoardState
import com.rdua.whiteboard.home.usecase.ICreateBoardUseCase
import com.rdua.whiteboard.home.usecase.IGetBoardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllBoardsViewModel @Inject constructor(
    private val getBoardsUseCase: IGetBoardsUseCase,
    private val getDeviceIdUseCase: GetDeviceIdUseCase,
    private val createBoardUseCase: ICreateBoardUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val navigationActions: IHomeNavigationActions,
    private val toastManager: ToastManager,
) : ViewModel() {

    var state by mutableStateOf(AllBoardsUIState())
        private set

    init {
        loadBoards()
    }

    fun onEvent(event: AllBoardsEvent) {
        when (event) {
            is AllBoardsEvent.NavigateToNewBoard -> createBoard(event.boardTitle)
            is AllBoardsEvent.NavigateToBoardItem -> navigateToBoardItem(id = event.boardId)
            is AllBoardsEvent.OpenBoardOptions -> openBoardOptions(event.boardEntity)
            is AllBoardsEvent.CloseBoardOptions -> closeBoardOptions()
            is AllBoardsEvent.MoreSettingsEvent -> { /*TODO*/ }
        }
    }

    private fun loadBoards() {
        viewModelScope.launch {
            firebaseAuth.currentUserId()?.let { currentUserId ->
                getBoardsUseCase.getBoards(currentUserId)
            }?.collect { boardList ->
                val boardOptionsItem = boardList?.find { it.id == state.boardOptionsBoardItem?.id }

                state = state.copy(
                    boardsList = boardList,
                    // Keep boardOptionsBoardItem up to date.
                    boardOptionsBoardItem = boardOptionsItem,
                    // Close board options if board is deleted by other user.
                    isOpenBoardOptions = boardOptionsItem != null,
                )
            }
        }
    }

    private fun createBoard(title: String) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUserId()
            val deviceId = getDeviceIdUseCase()

            val boardState = if (userId == null) {
                BoardState.CurrentUserIdFailure
            } else {
                val timestamp = Timestamp.now()
                val board = BoardEntity(
                    title = title,
                    creator = userId,
                    createdAt = timestamp.toEpochMilli(),
                    modifiedBy = UserUID(userId, deviceId),
                    modifiedAt = timestamp.toEpochMilli(),
                    users = mapOf(userId to AccessInfo(userId, AccessLevel.EDIT, timestamp.toEpochMilli()))
                )
                createBoardUseCase.createBoard(
                    boardUIModel = board, userBoardsTitles = state.boardsList?.map { it.title }
                )
            }
            checkBoardState(boardState = boardState)
        }
    }

    private suspend fun checkBoardState(boardState: BoardState) {
        when (boardState) {
            is BoardState.BoardSuccess -> navigateToBoardItem(id = boardState.boardId)

            is BoardState.CreateBoardFailure, BoardState.CurrentUserIdFailure,
            BoardState.KeyBoardFailure,
            -> toastManager.sendToast(ToastEvent(R.string.create_board_failure))
        }
    }

    private fun openBoardOptions(boardEntity: BoardEntity) {
        state = state.copy(
            boardOptionsBoardItem = boardEntity,
            isOpenBoardOptions = true,
        )
    }

    private fun closeBoardOptions() {
        state = state.copy(
            boardOptionsBoardItem = null,
            isOpenBoardOptions = false,
        )
    }

    private fun navigateToBoardItem(id: String?) {
        viewModelScope.launch {
            if (id == null) {
                toastManager.sendToast(ToastEvent(R.string.create_board_app_failure))
            } else {
                navigationActions.navigateToBoardItem(id = id)
            }
        }
    }
}