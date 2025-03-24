package com.rdua.whiteboard.profile_screen_non_edit.state

import com.rdua.whiteboard.data.entities.users.Gender

data class UserUI(

    val id: String = "",
    val photoUrl: String? = null,
    val name: String = "",
    val email: String = "",
    val mobileNumber: String? = null,
    val gender: Gender = Gender.PREFER_NOT_TO_SAY
)