package com.rdua.whiteboard.board_options_dialog.usecase

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface CheckBoardNameExistsUseCase {
    suspend fun checkBoardNameExists(boardName: String): FieldValidationResult
}