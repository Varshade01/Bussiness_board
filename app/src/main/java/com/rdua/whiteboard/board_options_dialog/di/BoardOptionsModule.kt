package com.rdua.whiteboard.board_options_dialog.di

import com.rdua.whiteboard.board_options_dialog.usecase.CheckBoardNameExistsUseCase
import com.rdua.whiteboard.board_options_dialog.usecase.CheckBoardNameExistsUseCaseImpl
import com.rdua.whiteboard.board_options_dialog.usecase.DeleteBoardUseCaseImpl
import com.rdua.whiteboard.board_options_dialog.usecase.DuplicateBoardUseCaseImpl
import com.rdua.whiteboard.board_options_dialog.usecase.IDeleteBoardUseCase
import com.rdua.whiteboard.board_options_dialog.usecase.IDuplicateBoardUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BoardOptionsModule {

    @Binds
    fun bindDeleteBoardUseCase(deleteBoardUseCase: DeleteBoardUseCaseImpl): IDeleteBoardUseCase

    @Binds
    fun bindDuplicateBoardUseCase(duplicateBoardUseCase: DuplicateBoardUseCaseImpl): IDuplicateBoardUseCase

    @Binds
    fun bindCheckBoardNameExistsUseCase(checkBoardNameExistsImpl: CheckBoardNameExistsUseCaseImpl): CheckBoardNameExistsUseCase
}
