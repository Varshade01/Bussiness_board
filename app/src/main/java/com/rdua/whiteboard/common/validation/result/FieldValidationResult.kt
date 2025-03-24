package com.rdua.whiteboard.common.validation.result

sealed interface FieldValidationResult {
    object Success : FieldValidationResult
    object InvalidName : FieldValidationResult
    object InvalidEmail : FieldValidationResult
    object InvalidPassword : FieldValidationResult
    object PasswordEmpty : FieldValidationResult
    object PasswordMismatch : FieldValidationResult
    object NewPasswordMatchesOld : FieldValidationResult
    object InvalidMobile: FieldValidationResult
    object InvalidMobileLength: FieldValidationResult
    object BoardNameExists : FieldValidationResult
    object TooManyCharacters : FieldValidationResult
}