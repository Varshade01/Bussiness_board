package com.rdua.whiteboard.repository.users

import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.firebase.state.GetUserState
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.firebase.state.UpdateUserState
import com.rdua.whiteboard.registration.model.RegistrationFields
import com.rdua.whiteboard.settings.state.LogoutState

interface UsersRepository {
    suspend fun createUser(
        state: RegistrationState,
        registrationFields: RegistrationFields
    ): RegistrationState

    suspend fun getUser(): GetUserState
    suspend fun getUser(id: String): GetUserState
    suspend fun getUserByEmail(email: String): GetUserState
    suspend fun updateUser(user: UserEntity): UpdateUserState
    suspend fun updateUserPicture(id: String, bytes: ByteArray): UpdateUserPictureState
    suspend fun deleteUserPicture(id: String): UpdateUserPictureState
    suspend fun logoutUser(): LogoutState
    suspend fun deleteUserFromDB(id: String)
    suspend fun deleteUserFromCache(id: String)
    fun updateFcmToken(id: String, token: String)
    fun removeFcmToken(id: String, token: String)
}
