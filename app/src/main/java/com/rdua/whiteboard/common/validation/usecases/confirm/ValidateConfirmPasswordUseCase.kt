package com.rdua.whiteboard.common.validation.usecases.confirm

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidateConfirmPasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): FieldValidationResult
}