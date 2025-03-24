package com.rdua.whiteboard.data.entities.boards

import android.os.Parcelable
import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BoardEntity(
    val id: String? = null,
    val title: String,
    val imageUrl: String? = null,
    val creator: String,
    val createdAt: Long,
    val modifiedBy: UserUID,
    val modifiedAt: Long,
    @IgnoredOnParcel val content: List<BoardItemModel>? = null,
    val users: Map<String, @RawValue AccessInfo> = mapOf()
): Parcelable, BoardEntityState