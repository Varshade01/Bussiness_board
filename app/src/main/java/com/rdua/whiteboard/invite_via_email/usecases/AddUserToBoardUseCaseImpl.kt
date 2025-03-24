package com.rdua.whiteboard.invite_via_email.usecases

import com.rdua.whiteboard.invite_via_email.state.AddUserToBoardState
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import com.rdua.whiteboard.data.entities.boards.access.AccessLevel
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class AddUserToBoardUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
) : IAddUserToBoardUseCase {
    override suspend operator fun invoke(boardId: String, userId: String): AddUserToBoardState {
        return boardsRepository.addUserToBoard(
            boardId = boardId,
            accessInfo = AccessInfo(userId, AccessLevel.READ_ONLY, Timestamp.now().toEpochMilli())
        )
    }
}