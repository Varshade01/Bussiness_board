package com.rdua.whiteboard.profile_screen_non_edit.events


sealed interface ProfileScreenEvent {

    object NavigateToProfileEditScreen : ProfileScreenEvent

    object LoadUser : ProfileScreenEvent
}