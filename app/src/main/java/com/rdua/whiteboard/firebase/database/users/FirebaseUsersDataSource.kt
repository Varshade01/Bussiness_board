package com.rdua.whiteboard.firebase.database.users

import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.firebase.state.GetUserState
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.firebase.state.UpdateUserState

interface FirebaseUsersDataSource {

    suspend fun createUserInFirebaseDB(id: String, name: String, email: String, temporaryUser: Boolean?): RegistrationState
    suspend fun getUserFromFirebase(id: String): GetUserState
    suspend fun getUserByEmailFromFirebase(email: String): GetUserState
    suspend fun updateUserInFirebaseDB(user: UserEntity): UpdateUserState
    suspend fun updateUserPicture(id: String, bytes: ByteArray): UpdateUserPictureState
    suspend fun deleteUserPicture(id: String): UpdateUserPictureState
    suspend fun deleteUserData(id: String): UpdateUserState
    suspend fun checkPhotoExists(id: String): Boolean
    fun updateFcmToken(id: String, token: String)
    fun removeFcmToken(id: String, token: String)
}
