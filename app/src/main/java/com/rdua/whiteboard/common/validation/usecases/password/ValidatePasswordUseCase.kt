package com.rdua.whiteboard.common.validation.usecases.password

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): FieldValidationResult
}