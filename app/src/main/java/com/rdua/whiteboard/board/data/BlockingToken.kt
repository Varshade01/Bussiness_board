package com.rdua.whiteboard.board.data

import com.rdua.whiteboard.common.time.Timestamp
import java.util.concurrent.TimeUnit


/**
 * Represent a token for a temporary block.
 *
 * @property userUID unique id of a user and their device.
 * @property timestamp time of blocking. Token might expire after certain time and block will be released.
 */
data class BlockingToken(val userUID: UserUID, val timestamp: Long) {
    /**
     * Checks whether the token has expired based on a given time and time unit.
     *
     * @param time The duration to compare against the token's creation time.
     * @param timeUnit The unit of time for the 'time' parameter.
     * @return `true` if the token has expired, `false` otherwise.
     */
    fun isTokenExpired(time: Long, timeUnit: TimeUnit) : Boolean {
        val currentTimeInMillis = Timestamp.now().toEpochMilli()
        val tokenExpiryInMillis = timestamp + timeUnit.toMillis(time)
        return currentTimeInMillis > tokenExpiryInMillis
    }
}