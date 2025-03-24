package com.rdua.whiteboard.common.validation.usecases.email

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidateEmailUseCase {
    operator fun invoke(email: String): FieldValidationResult
}