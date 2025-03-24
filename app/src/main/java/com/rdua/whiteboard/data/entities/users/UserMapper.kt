package com.rdua.whiteboard.data.entities.users

import com.rdua.whiteboard.profile_screen_non_edit.state.UserUI

object UserMapper {

    fun toUserEntity(firebaseUserEntity: FirebaseUserEntity): UserEntity {
        return UserEntity(
            id = firebaseUserEntity.id,
            name = firebaseUserEntity.name ?: "",
            email = firebaseUserEntity.email ?: "",
            photoUrl = firebaseUserEntity.photoUrl,
            mobileNumber = firebaseUserEntity.mobileNumber,
            gender = firebaseUserEntity.gender ?: Gender.PREFER_NOT_TO_SAY
        )
    }

    fun toFirebaseUserEntity(userEntity: UserEntity): FirebaseUserEntity {
        return FirebaseUserEntity(
            id = userEntity.id,
            name = userEntity.name,
            email = userEntity.email,
            photoUrl = userEntity.photoUrl,
            mobileNumber = userEntity.mobileNumber,
            gender = userEntity.gender
        )
    }

    fun toUserUI(userEntity: UserEntity): UserUI {
        return UserUI(
            id = userEntity.id,
            name = userEntity.name,
            email = userEntity.email,
            photoUrl = userEntity.photoUrl,
            mobileNumber = userEntity.mobileNumber,
            gender = userEntity.gender
        )
    }
}