package com.rdua.whiteboard.board_options_dialog.usecase

import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import com.rdua.whiteboard.data.entities.boards.FirebaseBoardEntity
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class CheckBoardNameExistsUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
    private val firebaseAuth: FirebaseAuthApi
) : CheckBoardNameExistsUseCase {

    override suspend fun checkBoardNameExists(boardName: String): FieldValidationResult {
        val list: List<FirebaseBoardEntity>? =
            boardsRepository.getBoards(firebaseAuth.currentUserId()!!).value
        val result = list?.any {
            it.title == boardName
        } ?: false

        return if (result) {
            FieldValidationResult.BoardNameExists
        } else FieldValidationResult.Success
    }
}