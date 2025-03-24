package com.rdua.whiteboard.common.validation.usecases.mobile

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidateMobileUseCase {
    operator fun invoke(mobile: String?) : FieldValidationResult
}