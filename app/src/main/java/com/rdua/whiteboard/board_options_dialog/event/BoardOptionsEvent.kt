package com.rdua.whiteboard.board_options_dialog.event

import com.rdua.whiteboard.data.entities.boards.BoardEntity

sealed interface BoardOptionsEvent {
    object RenameBoard : BoardOptionsEvent
    object InviteViaEmail : BoardOptionsEvent
    data class EnterInviteViaEmailText(val email: String) : BoardOptionsEvent
    object SharingOptions : BoardOptionsEvent
    object SharingLink : BoardOptionsEvent
    object Duplicate : BoardOptionsEvent
    object OpenDuplicateDialog : BoardOptionsEvent
    object CloseDuplicateDialog : BoardOptionsEvent
    object CloseRenameBoardDialog : BoardOptionsEvent
    object CloseInviteToBoardDialog:BoardOptionsEvent
    data class EnterRenameBoardText(val name: String) : BoardOptionsEvent
    object DeleteBoard : BoardOptionsEvent
    object DismissDeleteBoard : BoardOptionsEvent
    object OpenDeleteDialog : BoardOptionsEvent
    object OpenRenameBoardDialog : BoardOptionsEvent
    object OpenInviteViaEmailDialog : BoardOptionsEvent
    class UpdateBoardData(val boardEntity: BoardEntity): BoardOptionsEvent
}
