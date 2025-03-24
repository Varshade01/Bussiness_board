package com.rdua.whiteboard.common.validation.usecases.mobile

import android.util.Patterns
import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import javax.inject.Inject

class ValidateMobileUseCaseImpl @Inject constructor() : ValidateMobileUseCase {
    override fun invoke(mobile: String?): FieldValidationResult {
        if (mobile.isNullOrEmpty()){
            return FieldValidationResult.Success
        }
        return if (mobile.length > 15){
            FieldValidationResult.InvalidMobileLength
        } else if(!Patterns.PHONE.matcher(mobile).matches()) {
            FieldValidationResult.InvalidMobile
        } else {
            FieldValidationResult.Success
        }
    }
}