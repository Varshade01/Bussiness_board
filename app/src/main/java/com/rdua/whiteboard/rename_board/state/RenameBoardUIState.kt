package com.rdua.whiteboard.rename_board.state

import com.rdua.whiteboard.rename_board.utils.StringResource

data class RenameBoardUIState(
    val boardNameText: String = "",
    val boardNameIsError: Boolean = false,
    val boardNameErrorTextResource: StringResource? = null
)