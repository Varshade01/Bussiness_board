package com.rdua.whiteboard.board_options_dialog.usecase

import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.ValueModel
import com.rdua.whiteboard.board_options_dialog.state.DuplicateBoardState
import com.rdua.whiteboard.common.provider.ResourceProvider
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.common.usecases.GetDeviceIdUseCase
import com.rdua.whiteboard.common.usecases.IGetUserUseCase
import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.data.entities.boards.BoardMapper
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import com.rdua.whiteboard.data.entities.boards.access.AccessLevel
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.home.state.BoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class DuplicateBoardUseCaseImpl @Inject constructor(
    private val checkBoardNameExistsUseCase: CheckBoardNameExistsUseCase,
    private val getUserUseCase: IGetUserUseCase,
    private val getDeviceIdUseCase: GetDeviceIdUseCase,
    private val resourceProvider: ResourceProvider,
    private val boardsRepository: BoardsRepository,
    private val boardMapper: BoardMapper,
) : IDuplicateBoardUseCase {

    override suspend operator fun invoke(boardData: BoardEntity): DuplicateBoardState {
        val newBoard = duplicateBoardEntity(boardData) ?: return DuplicateBoardState.Failure
        val firebaseBoardEntity = boardMapper.toFirebaseEntity(newBoard)

        return when(val result = boardsRepository.createBoard(firebaseBoardEntity)) {
            is BoardState.BoardSuccess -> DuplicateBoardState.Success(result.boardId)
            else -> DuplicateBoardState.Failure
        }
    }

    /**
     * Board duplicate has current user as creator for all items, updated modify information and new
     * title. All blocked item are unblocked.
     */
    private suspend fun duplicateBoardEntity(boardData: BoardEntity) : BoardEntity? {
        val newTitle = getNewBoardTitle(boardData.title)
        val timestamp = Timestamp.now()
        val userId = getCurrentUserId() ?: return null
        val userUID = UserUID(
            userId = userId,
            deviceId = getDeviceIdUseCase(),
        )

        return BoardEntity(
            title = newTitle,
            creator = userId,
            createdAt = timestamp.toEpochMilli(),
            modifiedBy = userUID,
            modifiedAt = timestamp.toEpochMilli(),
            content = duplicateBoardContent(
                boardContent = boardData.content,
                creatorId = userUID.userId,
                timestamp = timestamp
            ),
            users = mapOf(userId to AccessInfo(userId, AccessLevel.EDIT, timestamp.toEpochMilli())),
        )
    }

    /**
     * After duplication all board items should have [creatorId] as "Creator" and "Last modifying user".
     * And current [Timestamp] as "Created time" and "Last modified time".
     *
     * Also, items should be unblocked.
     *
     * Selected and EditMode state will be discarded during mapping to FirebaseBoardEntity. StickyNote
     * author will be resolved by creatorId during mapping to BoardEntity.
     */
    private fun duplicateBoardContent(
        boardContent: List<BoardItemModel>?,
        creatorId: String,
        timestamp: Timestamp
    ): List<BoardItemModel>? {
        return boardContent?.map { item ->
            item.toCopy(
                ValueModel.BlockedBy(isBlockedBy = null),
                ValueModel.BlockToken(blockingToken = null),
                ValueModel.Creator(creator = creatorId),
                ValueModel.CreatedAt(timestamp = timestamp),
                ValueModel.ModifiedBy(modifiedBy = creatorId),
                ValueModel.ModifiedAt(timestamp = timestamp),
            )
        }
    }

    private suspend fun getCurrentUserId(): String? {
        return when(val result = getUserUseCase.getCurrentUser()) {
            is GetUserStateSuccess -> result.user.id
            else -> null
        }
    }

    /**
     * New board title rules:
     * 1. First duplicate adds suffix "- Copy".
     * 2. If board with current title and "- Copy" suffix exists, add "- Copy (#)", where # is the
     * first non-taken ordinal.
     */
    private suspend fun getNewBoardTitle(currentTitle: String): String {
        var newTitle = "$currentTitle - ${resourceProvider.getString(R.string.copy)}"
        var count = 2

        while(isBoardNameTaken(newTitle)) {
            newTitle = "$currentTitle - ${resourceProvider.getString(R.string.copy_with_count, count)}"
            count++
        }
        return newTitle
    }

    private suspend fun isBoardNameTaken(boardTitle: String): Boolean {
        return checkBoardNameExistsUseCase.checkBoardNameExists(boardTitle) != FieldValidationResult.Success
    }
}