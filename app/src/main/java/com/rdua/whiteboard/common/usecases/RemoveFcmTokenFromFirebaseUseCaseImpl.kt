package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.data.data_store.SettingsDataStore
import com.rdua.whiteboard.di.IoCoroutineScope
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.repository.users.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoveFcmTokenFromFirebaseUseCaseImpl @Inject constructor(
    private val userRepository: UsersRepository,
    private val settingsDataStore: SettingsDataStore,
    private val fireBaseAuth: FirebaseAuthApi,
    @IoCoroutineScope
    private val coroutineScope: CoroutineScope,
) : RemoveFcmTokenUseCase {

    override fun removeFcmToken() {
        val userId = fireBaseAuth.currentUserId() ?: return

        coroutineScope.launch {
            val token = settingsDataStore.getFcmToken()
            if (token != null) {
                userRepository.removeFcmToken(userId, token)
            }
        }
    }
}