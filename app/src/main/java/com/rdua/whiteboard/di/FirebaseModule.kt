package com.rdua.whiteboard.di

import com.rdua.whiteboard.firebase.auth.FirebaseAuth
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.connection.FirebaseConnection
import com.rdua.whiteboard.firebase.connection.FirebaseConnectionImpl
import com.rdua.whiteboard.firebase.database.boards.FirebaseBoardsDataSourceImpl
import com.rdua.whiteboard.firebase.database.boards.IFirebaseBoardsDataSource
import com.rdua.whiteboard.firebase.database.users.FirebaseUsersDataSource
import com.rdua.whiteboard.firebase.database.users.FirebaseUsersDataSourceImpl
import com.rdua.whiteboard.firebase.invitation_link.FirebaseDynamicLink
import com.rdua.whiteboard.firebase.invitation_link.FirebaseDynamicLinkAPI
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {

    @Binds
    abstract fun bindFirebaseAuth(firebaseAuth: FirebaseAuth): FirebaseAuthApi

    @Binds
    abstract fun bindFirebaseUsersDataSource(firebaseUsersDataSourceImpl: FirebaseUsersDataSourceImpl):
            FirebaseUsersDataSource

    @Binds
    abstract fun bindFirebaseConnection(firebaseConnectionImpl: FirebaseConnectionImpl): FirebaseConnection

    @Binds
    abstract fun bindFirebaseBoardsDataSource(firebaseBoardsDataSourceImpl: FirebaseBoardsDataSourceImpl):
            IFirebaseBoardsDataSource
    @Binds
    abstract fun bindFirebaseDynamicLinkApi(firebaseDynamicLink: FirebaseDynamicLink): FirebaseDynamicLinkAPI

}
