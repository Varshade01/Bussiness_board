package com.rdua.whiteboard.data.entities.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String? = null,
    val mobileNumber: String? = null,
    val gender: Gender = Gender.PREFER_NOT_TO_SAY
)