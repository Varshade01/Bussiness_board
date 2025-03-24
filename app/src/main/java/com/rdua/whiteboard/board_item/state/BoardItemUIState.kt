package com.rdua.whiteboard.board_item.state

import com.rdua.whiteboard.board_item.model.BoardItemInfo

data class BoardItemUIState(
    val searchText: String = "",
    val fontSizeInput: String = "",
    val isOpenShapesBottomSheet: Boolean = false,
    val isOpenBoardOptions: Boolean = false,
    val isOpenBorderColorBar: Boolean = false,
    val isOpenBackgroundColorBar: Boolean = false,
    val isOpenFontColorBar: Boolean = false,
    val isOpenTextToolbar: Boolean = false,
    val isOpenThreeDotsToolbar: Boolean = false,
    val isOpenFontSizeToolbar: Boolean = false,
    val isOpenBoardItemInfoBar: Boolean = false,
    val selectedItemInfo: BoardItemInfo = BoardItemInfo(),
)