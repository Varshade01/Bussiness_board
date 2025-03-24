package com.rdua.whiteboard.common.validation.usecases.email

import android.util.Patterns
import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import javax.inject.Inject

class ValidateEmailUseCaseImpl @Inject constructor() : ValidateEmailUseCase {
    override operator fun invoke(email: String): FieldValidationResult {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FieldValidationResult.InvalidEmail
        } else {
            FieldValidationResult.Success
        }
    }
}