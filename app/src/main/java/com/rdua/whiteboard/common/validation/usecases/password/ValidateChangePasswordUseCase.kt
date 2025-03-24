package com.rdua.whiteboard.common.validation.usecases.password

import com.rdua.whiteboard.common.validation.result.FieldValidationResult

interface ValidateChangePasswordUseCase {
    operator fun invoke(currentPassword: String, newPassword: String): FieldValidationResult
}