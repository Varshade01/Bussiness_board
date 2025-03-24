package com.rdua.whiteboard.di

import com.rdua.whiteboard.repository.users.UsersRepository
import com.rdua.whiteboard.repository.users.UsersRepositoryImpl
import com.rdua.whiteboard.repository.boards.BoardsRepository
import com.rdua.whiteboard.repository.boards.BoardsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUsersRepository(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository

    @Singleton
    @Binds
    abstract fun bindBoardsRepository(boardsRepositoryImpl: BoardsRepositoryImpl): BoardsRepository
}
