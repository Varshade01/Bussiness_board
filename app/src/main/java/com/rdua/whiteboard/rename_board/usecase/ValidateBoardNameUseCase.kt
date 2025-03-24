package com.rdua.whiteboard.rename_board.usecase

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidateBoardNameUseCase {

    operator fun invoke(name: String): FieldValidationResult
}