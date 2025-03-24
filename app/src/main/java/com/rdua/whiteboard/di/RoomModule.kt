package com.rdua.whiteboard.di

import android.content.Context
import androidx.room.Room
import com.rdua.whiteboard.data.room_database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "whiteboard-db").build()

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase) = appDatabase.usersDao()
}