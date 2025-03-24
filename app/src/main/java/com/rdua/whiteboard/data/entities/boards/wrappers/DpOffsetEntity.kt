package com.rdua.whiteboard.data.entities.boards.wrappers

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.annotations.AutoMap

@AutoMap
data class DpOffsetEntity(val x: Float = 0f, val y: Float = 0f)

fun DpOffset.toDpOffsetEntity() = DpOffsetEntity(x = this.x.value, y = this.y.value)
fun DpOffsetEntity.toDpOffset() = DpOffset(x = this.x.dp, y = this.y.dp)
