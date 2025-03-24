package com.rdua.whiteboard.data.entities.users

data class FirebaseUserEntity(
    val id: String = "",
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val mobileNumber: String? = null,
    val isDeleted:Boolean? = null,
    val gender: Gender? = null,
    val temporaryUser: Boolean? = null,
)