package com.rdua.whiteboard.settings.di

import com.rdua.whiteboard.settings.usecase.ClearBoardsCacheUseCaseImpl
import com.rdua.whiteboard.settings.usecase.DeleteAccountUseCase
import com.rdua.whiteboard.settings.usecase.DeleteAccountUseCaseImpl
import com.rdua.whiteboard.settings.usecase.IClearBoardsCacheUseCase
import com.rdua.whiteboard.settings.usecase.ILogoutUseCase
import com.rdua.whiteboard.settings.usecase.LogoutUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {

    @Binds
    fun bindLogoutUseCase(logoutUseCase: LogoutUseCaseImpl): ILogoutUseCase

    @Binds
    fun bindClearBoardsCacheUseCase(
        clearBoardsCacheUseCase: ClearBoardsCacheUseCaseImpl
    ): IClearBoardsCacheUseCase

    @Binds
    fun bindDeleteAccountUseCase(
        deleteAccountUseCaseImpl: DeleteAccountUseCaseImpl
    ): DeleteAccountUseCase
}
