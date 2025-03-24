package com.rdua.whiteboard.common.validation.usecases.name

import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import javax.inject.Inject

class ValidateNameUseCaseImpl @Inject constructor() : ValidateNameUseCase {
    override operator fun invoke(name: String): FieldValidationResult {
        return if (name.length !in 1..100 || name.isBlank()) {
            FieldValidationResult.InvalidName
        } else {
            FieldValidationResult.Success
        }
    }
}