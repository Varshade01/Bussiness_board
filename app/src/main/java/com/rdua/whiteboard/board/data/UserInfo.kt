package com.rdua.whiteboard.board.data

/**
 * Represents user information.
 *
 * @property name The name of the user.
 * @property isThisUser Indicates whether this is the save user (possibly on other device).
 * @property photoUrl The URL of the user's photo.
 */
data class UserInfo(val name: String? = null, val isThisUser: Boolean = false, val photoUrl: String? = null)