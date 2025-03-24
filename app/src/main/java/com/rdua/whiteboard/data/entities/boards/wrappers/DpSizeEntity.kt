package com.rdua.whiteboard.data.entities.boards.wrappers

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.annotations.AutoMap

@AutoMap
data class DpSizeEntity(val width: Float = 0f, val height: Float = 0f)

fun DpSize.toDpSizeEntity() = DpSizeEntity(width = this.width.value, height = this.height.value)
fun DpSizeEntity.toDpSize() = DpSize(width = this.width.dp, height = this.height.dp)