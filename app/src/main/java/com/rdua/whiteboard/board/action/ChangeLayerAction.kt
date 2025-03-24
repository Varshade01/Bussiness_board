package com.rdua.whiteboard.board.action

sealed interface ChangeLayerAction {

    object BringToFront : ChangeLayerAction
    object SendToBack : ChangeLayerAction
}
