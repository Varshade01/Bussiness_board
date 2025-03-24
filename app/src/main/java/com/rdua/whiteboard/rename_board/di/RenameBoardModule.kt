package com.rdua.whiteboard.rename_board.di

import com.rdua.whiteboard.rename_board.usecase.RenameBoardUseCase
import com.rdua.whiteboard.rename_board.usecase.RenameBoardUseCaseImpl
import com.rdua.whiteboard.rename_board.usecase.ValidateBoardNameUseCase
import com.rdua.whiteboard.rename_board.usecase.ValidateBoardNameUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RenameBoardModule {

    @Binds
    abstract fun bindRenameBoardUseCase(renameBoardUseCaseImpl: RenameBoardUseCaseImpl): RenameBoardUseCase

    @Binds
    abstract fun bindValidateBoardNameLength(validateBoardNameUseCaseImpl: ValidateBoardNameUseCaseImpl): ValidateBoardNameUseCase
}