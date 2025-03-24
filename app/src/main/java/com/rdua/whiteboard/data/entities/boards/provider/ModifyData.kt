package com.rdua.whiteboard.data.entities.boards.provider

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.data.UserUID


@AutoMap
data class ModifyData(
    val modifiedBy: UserUID,
    val modifiedAt: Long,
)