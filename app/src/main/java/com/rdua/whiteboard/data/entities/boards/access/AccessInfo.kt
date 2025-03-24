package com.rdua.whiteboard.data.entities.boards.access

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AccessInfo(
    val userId: String = "",
    val accessLevel: AccessLevel = AccessLevel.READ_ONLY,
    val timestamp: Long = 0L
) : Parcelable