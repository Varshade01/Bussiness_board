package com.rdua.whiteboard.common.validation.usecases.password

import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import javax.inject.Inject

class ValidatePasswordUseCaseImpl @Inject constructor() : ValidatePasswordUseCase {
    override operator fun invoke(password: String): FieldValidationResult {
        return if (password.length !in 6..16) {
            FieldValidationResult.InvalidPassword
        } else {
            FieldValidationResult.Success
        }
    }
}