package com.rdua.whiteboard.board.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represent Unique User Identifier for this device.
 *
 * @property userId unique id of a user.
 * @property deviceId unique id of that user's device (or session). Will change, if user would delete
 * application data or reinstall the app.
 */
@Parcelize
data class UserUID(val userId: String = "", val deviceId: String = "") : Parcelable