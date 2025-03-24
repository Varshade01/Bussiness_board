package com.rdua.whiteboard.data.entities.boards.wrappers

import com.rdua.whiteboard.common.time.Timestamp

fun Timestamp.toJson() = toEpochMilli().toString()

fun String.fromTimestampJson() : Timestamp? {
    val time =  try {
        toLong()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return time?.let { Timestamp.fromEpochMilli(time) }
}