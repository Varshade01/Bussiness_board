package com.rdua.whiteboard.data.entities.boards

import android.os.Parcelable
import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

// Needs to have a default constructor.
@Parcelize
data class FirebaseBoardEntity(
    val id: String? = null,
    val title: String? = null,
    val imageUrl: String? = null,
    val creator: String? = null,
    val createdAt: Long? = null,
    val modifiedBy: UserUID? = null,
    val modifiedAt: Long? = null,
    val content: Map<String, Map<String, @RawValue Any?>>? = null,
    val users: Map<String, @RawValue AccessInfo> = mapOf()
) : Parcelable