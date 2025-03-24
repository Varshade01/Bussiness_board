package com.rdua.whiteboard.data.entities.users

import com.rdua.whiteboard.R

enum class Gender {
    MALE, FEMALE, PREFER_NOT_TO_SAY
}

fun Gender.getResId():Int {
    return when(this) {
        Gender.MALE -> R.string.gender_male
        Gender.FEMALE -> R.string.gender_female
        Gender.PREFER_NOT_TO_SAY -> R.string.gender_prefer_not_to_say
    }
}