package com.rdua.whiteboard.board_item.event

sealed interface ThreeDotsEvent {

    data class CopyLink(val id: String) : ThreeDotsEvent
    data class Lock(val id: String) : ThreeDotsEvent
    data class Unlock(val id: String) : ThreeDotsEvent
    data class CreateFrame(val id: String) : ThreeDotsEvent
    data class BringFront(val id: String) : ThreeDotsEvent
    data class SendBack(val id: String) : ThreeDotsEvent
    data class ShowInfo(val id: String) : ThreeDotsEvent
    object HideInfo : ThreeDotsEvent

    object OpenToolbar : ThreeDotsEvent
    object CloseToolbar : ThreeDotsEvent
}