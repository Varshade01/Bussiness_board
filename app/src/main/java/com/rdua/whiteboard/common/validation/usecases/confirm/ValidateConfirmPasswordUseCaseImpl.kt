package com.rdua.whiteboard.common.validation.usecases.confirm

import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import javax.inject.Inject

class ValidateConfirmPasswordUseCaseImpl @Inject constructor() : ValidateConfirmPasswordUseCase {
    override operator fun invoke(password: String, confirmPassword: String): FieldValidationResult {
        return if (confirmPassword.isEmpty()) {
            FieldValidationResult.PasswordEmpty
        } else if (password != confirmPassword) {
            FieldValidationResult.PasswordMismatch
        } else {
            FieldValidationResult.Success
        }
    }
}