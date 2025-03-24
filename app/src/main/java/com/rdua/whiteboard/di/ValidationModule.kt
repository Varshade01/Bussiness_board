package com.rdua.whiteboard.di

import com.rdua.whiteboard.common.validation.usecases.confirm.ValidateConfirmPasswordUseCase
import com.rdua.whiteboard.common.validation.usecases.confirm.ValidateConfirmPasswordUseCaseImpl
import com.rdua.whiteboard.common.validation.usecases.email.ValidateEmailUseCase
import com.rdua.whiteboard.common.validation.usecases.email.ValidateEmailUseCaseImpl
import com.rdua.whiteboard.common.validation.usecases.mobile.ValidateMobileUseCase
import com.rdua.whiteboard.common.validation.usecases.mobile.ValidateMobileUseCaseImpl
import com.rdua.whiteboard.common.validation.usecases.name.ValidateNameUseCase
import com.rdua.whiteboard.common.validation.usecases.name.ValidateNameUseCaseImpl
import com.rdua.whiteboard.common.validation.usecases.password.ValidateChangePasswordUseCase
import com.rdua.whiteboard.common.validation.usecases.password.ValidateChangePasswordUseCaseImpl
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCase
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ValidationModule {
    @Singleton
    @Binds
    abstract fun bindValidateNameUseCase(
        validateNameUseCaseImpl: ValidateNameUseCaseImpl
    ): ValidateNameUseCase

    @Singleton
    @Binds
    abstract fun bindValidateEmailUseCase(
        validateEmailUseCaseImpl: ValidateEmailUseCaseImpl
    ): ValidateEmailUseCase

    @Singleton
    @Binds
    abstract fun bindValidatePasswordUseCase(
        validatePasswordUseCaseImpl: ValidatePasswordUseCaseImpl
    ): ValidatePasswordUseCase

    @Singleton
    @Binds
    abstract fun bindValidateChangePasswordUseCase(
        validateChangePasswordUseCaseImpl: ValidateChangePasswordUseCaseImpl
    ): ValidateChangePasswordUseCase

    @Singleton
    @Binds
    abstract fun bindValidateConfirmPasswordUseCase(
        validateConfirmPasswordUseCaseImpl: ValidateConfirmPasswordUseCaseImpl
    ): ValidateConfirmPasswordUseCase

    @Singleton
    @Binds
    abstract fun bindValidateMobileUseCase(
        validateMobileUseCaseImpl: ValidateMobileUseCaseImpl
    ) : ValidateMobileUseCase
}