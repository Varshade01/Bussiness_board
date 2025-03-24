package com.rdua.whiteboard.common.validation.usecases.password

import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import javax.inject.Inject

class ValidateChangePasswordUseCaseImpl @Inject constructor(
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ValidateChangePasswordUseCase {
    override fun invoke(currentPassword: String, newPassword: String): FieldValidationResult {
        return when (val validationResult = validatePasswordUseCase(newPassword)) {
            FieldValidationResult.Success -> validatePasswordsMatching(currentPassword, newPassword)
            else -> validationResult
        }
    }

    private fun validatePasswordsMatching(
        currentPassword: String,
        newPassword: String
    ): FieldValidationResult {
        return if (currentPassword == newPassword) {
            FieldValidationResult.NewPasswordMatchesOld
        } else {
            FieldValidationResult.Success
        }
    }
}