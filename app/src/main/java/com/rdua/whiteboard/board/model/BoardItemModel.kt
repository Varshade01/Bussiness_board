package com.rdua.whiteboard.board.model

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.rdua.whiteboard.board.constants.BlockingPadding
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.common.time.Timestamp


sealed interface BoardItemModel {
    val id: String
    val size: DpSize
    val coordinate: DpOffset
    val isSelected: Boolean
    val isEditMode: Boolean
    val isLocked: Boolean
    val isBlockedBy: UserInfo?
    val blockingToken: BlockingToken?
    val creator: String
    val createdAt: Timestamp
    val modifiedBy: String
    val modifiedAt: Timestamp
    val blockingPadding: BlockingPadding

    fun toCopy(vararg values: ValueModel): BoardItemModel

    fun isModifiedAfter(other: BoardItemModel): Boolean {
        return this.modifiedAt.isAfter(other.modifiedAt)
    }
}
