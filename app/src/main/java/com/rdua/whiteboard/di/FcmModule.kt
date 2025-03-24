package com.rdua.whiteboard.di

import com.rdua.whiteboard.common.usecases.RemoveFcmTokenFromFirebaseUseCaseImpl
import com.rdua.whiteboard.common.usecases.RemoveFcmTokenUseCase
import com.rdua.whiteboard.common.usecases.SaveFcmTokenToDataStoreUseCase
import com.rdua.whiteboard.common.usecases.SaveFcmTokenToDataStoreUseCaseImpl
import com.rdua.whiteboard.common.usecases.UpdateFcmTokenToFirebaseUseCase
import com.rdua.whiteboard.common.usecases.UpdateFcmTokenToFirebaseUseCaseImpl
import com.rdua.whiteboard.data.data_store.SettingsDataStore
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.repository.users.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
class FcmModule {
    @Provides
    fun provideUpdateFcmTokenUseCase(
        userRepository: UsersRepository,
        dataStoreManager: SettingsDataStore,
        fireBaseAuth: FirebaseAuthApi,
        @IoCoroutineScope coroutineScope: CoroutineScope
    ): UpdateFcmTokenToFirebaseUseCase = UpdateFcmTokenToFirebaseUseCaseImpl(
        userRepository,
        dataStoreManager,
        fireBaseAuth,
        coroutineScope
    )

    @Provides
    fun provideRemoveFcmTokenUseCase(
        userRepository: UsersRepository,
        dataStoreManager: SettingsDataStore,
        fireBaseAuth: FirebaseAuthApi,
        @IoCoroutineScope coroutineScope: CoroutineScope
    ): RemoveFcmTokenUseCase = RemoveFcmTokenFromFirebaseUseCaseImpl(
        userRepository,
        dataStoreManager,
        fireBaseAuth,
        coroutineScope
    )

    @Provides
    fun provideSaveFcmTokenToDataStoreUseCase(
        settingsDataStore: SettingsDataStore,
        @IoCoroutineScope coroutineScope: CoroutineScope,
    ): SaveFcmTokenToDataStoreUseCase = SaveFcmTokenToDataStoreUseCaseImpl(
        settingsDataStore,
        coroutineScope
    )
}