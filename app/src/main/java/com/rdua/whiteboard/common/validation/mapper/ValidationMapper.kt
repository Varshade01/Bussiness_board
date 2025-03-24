package com.rdua.whiteboard.common.validation.mapper

import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import com.rdua.whiteboard.common.validation.state.ValidationState

object ValidationMapper {
    fun toValidationState(result: FieldValidationResult): ValidationState {
        var isError = true
        val messageId: Int? = when (result) {
            is FieldValidationResult.InvalidName -> R.string.invalid_name
            is FieldValidationResult.InvalidEmail -> R.string.invalid_email_format
            is FieldValidationResult.InvalidPassword -> R.string.invalid_password
            is FieldValidationResult.PasswordEmpty -> R.string.confirm_passwords_empty
            is FieldValidationResult.PasswordMismatch -> R.string.confirm_passwords_mismatch
            is FieldValidationResult.NewPasswordMatchesOld -> R.string.new_password_cannot_be_same_as_old_password
            is FieldValidationResult.InvalidMobile -> R.string.invalid_mobile
            is FieldValidationResult.InvalidMobileLength -> R.string.invalid_mobile_length
            is FieldValidationResult.BoardNameExists -> R.string.name_is_taken
            is FieldValidationResult.TooManyCharacters -> R.string.too_many_characters
            is FieldValidationResult.Success -> {
                isError = false
                null
            }
        }
        return ValidationState(isError, messageId)
    }
}