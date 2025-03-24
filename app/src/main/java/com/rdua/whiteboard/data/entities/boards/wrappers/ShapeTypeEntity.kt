package com.rdua.whiteboard.data.entities.boards.wrappers

import com.rdua.whiteboard.board.model.ShapeType

fun ShapeType.toJson() = this.toString()

fun String.fromShapeTypeJson(): ShapeType = ShapeType.valueOf(this)