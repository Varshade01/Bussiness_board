package com.rdua.whiteboard.data.entities.boards.wrappers

import com.example.annotations.AutoMap
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserUID

@AutoMap
data class BlockingTokenEntity(val userId: String, val deviceId: String, val timestamp: Long)

fun BlockingToken.toEntity() = BlockingTokenEntity(
    userId = userUID.userId,
    deviceId = userUID.deviceId,
    timestamp = timestamp,
)

fun BlockingTokenEntity.toBlockingToken() = BlockingToken(
    userUID = UserUID(
        userId = userId,
        deviceId = deviceId
    ),
    timestamp = timestamp,
)