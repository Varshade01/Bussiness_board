package com.rdua.whiteboard.board_item.screen.toolbar

sealed class NoteToolBarOptions {
    object Color : NoteToolBarOptions()
    object Text : NoteToolBarOptions()
    object Copy : NoteToolBarOptions()
    object Divider : NoteToolBarOptions()
    object Delete : NoteToolBarOptions()
    object Others : NoteToolBarOptions()
    object Border : NoteToolBarOptions()
    object Lock : NoteToolBarOptions()
}
