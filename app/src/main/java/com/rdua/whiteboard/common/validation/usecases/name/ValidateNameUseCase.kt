package com.rdua.whiteboard.common.validation.usecases.name

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidateNameUseCase {
    operator fun invoke(name: String): FieldValidationResult
}