package com.rdua.whiteboard.data.entities.boards.wrappers

import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import com.example.annotations.AutoMap

@AutoMap
data class VerticalTextAlignmentEntity(
    val verticalBias: Float?,
)

fun Alignment.toVerticalTextAlignmentEntity(): VerticalTextAlignmentEntity =
    VerticalTextAlignmentEntity(
        verticalBias = when (this) {
            Alignment.TopCenter -> -1f
            Alignment.BottomCenter -> 1f
            else -> 0f
        },
    )

fun VerticalTextAlignmentEntity.toVerticalTextAlignment(): BiasAlignment? =
    verticalBias?.let {
        BiasAlignment(
            horizontalBias = 0f,  // default value horizontal center alignment,
            verticalBias = it,
        )
    }
