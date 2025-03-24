package com.rdua.whiteboard.rename_board.usecase

import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import com.rdua.whiteboard.rename_board.validation.BoardNameValidationRules
import javax.inject.Inject

class ValidateBoardNameUseCaseImpl @Inject constructor(
    private val boardNameValidationRules: BoardNameValidationRules,
) : ValidateBoardNameUseCase {
    override fun invoke(name: String): FieldValidationResult {
        return if (name.isBlank() || name.isEmpty()) {
            FieldValidationResult.InvalidName
        } else if (name.length > boardNameValidationRules.maxLength) {
            FieldValidationResult.TooManyCharacters
        } else {
            FieldValidationResult.Success
        }
    }
}