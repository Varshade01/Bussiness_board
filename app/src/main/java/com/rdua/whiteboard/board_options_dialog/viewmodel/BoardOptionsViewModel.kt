package com.rdua.whiteboard.board_options_dialog.viewmodel

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsDismissEvent
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsEvent
import com.rdua.whiteboard.board_options_dialog.navigation.BoardOptionsNavigationActions
import com.rdua.whiteboard.board_options_dialog.state.DeleteBoardState
import com.rdua.whiteboard.board_options_dialog.state.DuplicateBoardState
import com.rdua.whiteboard.board_options_dialog.usecase.CheckBoardNameExistsUseCase
import com.rdua.whiteboard.board_options_dialog.usecase.IDeleteBoardUseCase
import com.rdua.whiteboard.board_options_dialog.usecase.IDuplicateBoardUseCase
import com.rdua.whiteboard.board_options_dialog.viewmodel.BoardOptionsViewModel.Companion.KEY
import com.rdua.whiteboard.common.manager.snackbar.SnackbarEvent
import com.rdua.whiteboard.common.manager.snackbar.SnackbarManager
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.usecases.IGetUserUseCase
import com.rdua.whiteboard.common.utils.DEFAULT_ARGS
import com.rdua.whiteboard.common.utils.viewModel
import com.rdua.whiteboard.common.validation.mapper.ValidationMapper
import com.rdua.whiteboard.common.validation.result.FieldValidationResult
import com.rdua.whiteboard.common.validation.usecases.email.ValidateEmailUseCase
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.firebase.state.GetUserStateUserNotFound
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.firebase.utils.toMessageId
import com.rdua.whiteboard.invite_via_email.state.InviteViaEmailToBoardUIState
import com.rdua.whiteboard.invite_via_email.usecases.IAddUserToBoardUseCase
import com.rdua.whiteboard.registration.usecases.ICreateUserUseCase
import com.rdua.whiteboard.rename_board.state.RenameBoardState
import com.rdua.whiteboard.rename_board.state.RenameBoardUIState
import com.rdua.whiteboard.rename_board.usecase.RenameBoardUseCase
import com.rdua.whiteboard.rename_board.usecase.ValidateBoardNameUseCase
import com.rdua.whiteboard.rename_board.utils.StringResource
import com.rdua.whiteboard.rename_board.validation.BoardNameValidationRules
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CHARACTERS_LEFT_WARNING_THRESHOLD = 5

@HiltViewModel
class BoardOptionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deleteBoardUseCase: IDeleteBoardUseCase,
    private val checkBoardNameExistsUseCase: CheckBoardNameExistsUseCase,
    private val renameBoardUseCase: RenameBoardUseCase,
    private val validateBoardNameUseCase: ValidateBoardNameUseCase,
    private val duplicateBoardUseCase: IDuplicateBoardUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val getUserUseCase: IGetUserUseCase,
    private val addUserToBoardUseCase: IAddUserToBoardUseCase,
    private val boardNameValidationRules: BoardNameValidationRules,
    private val navigationActions: BoardOptionsNavigationActions,
    private val toastManager: ToastManager,
    private val snackbarManager: SnackbarManager,
    private val createUserUseCase: ICreateUserUseCase
) : ViewModel() {

    private var boardData: BoardEntity

    var isOpenDeleteDialog by mutableStateOf(false)
        private set

    var isOpenRenameDialog by mutableStateOf(false)
        private set
    var boardNameState by mutableStateOf(RenameBoardUIState())

    var isOpenInviteViaEmailDialog by mutableStateOf(false)
    var inviteViaEmailState by mutableStateOf(InviteViaEmailToBoardUIState())

    var isOpenDuplicateDialog by mutableStateOf(false)
        private set

    private val _callbackEventFlow = MutableSharedFlow<BoardOptionsDismissEvent>()
    val callbackEventFlow = _callbackEventFlow.asSharedFlow()

    init {
        boardData = checkNotNull(savedStateHandle.get<BoardEntity>(DEFAULT_ARGS))
    }

    fun onEvent(event: BoardOptionsEvent) {
        when (event) {
            is BoardOptionsEvent.RenameBoard -> renameBoard()
            is BoardOptionsEvent.InviteViaEmail -> inviteViaEmailToBoard()
            is BoardOptionsEvent.EnterInviteViaEmailText -> updateInviteViaEmailState(email = event.email)
            is BoardOptionsEvent.CloseInviteToBoardDialog -> closeInviteViaEmailDialog()
            is BoardOptionsEvent.OpenInviteViaEmailDialog -> isOpenInviteViaEmailDialog = true
            is BoardOptionsEvent.SharingOptions -> {/*TODO*/ }
            is BoardOptionsEvent.SharingLink -> {/*TODO*/ }
            is BoardOptionsEvent.Duplicate -> duplicateBoard()
            is BoardOptionsEvent.OpenDuplicateDialog -> isOpenDuplicateDialog = true
            is BoardOptionsEvent.CloseDuplicateDialog -> isOpenDuplicateDialog = false
            is BoardOptionsEvent.DeleteBoard -> deleteBoard()
            is BoardOptionsEvent.DismissDeleteBoard -> isOpenDeleteDialog = false
            is BoardOptionsEvent.OpenDeleteDialog -> isOpenDeleteDialog = true
            is BoardOptionsEvent.CloseRenameBoardDialog -> isOpenRenameDialog = false
            is BoardOptionsEvent.OpenRenameBoardDialog -> openRenameBoardDialog()
            is BoardOptionsEvent.EnterRenameBoardText -> updateRenameBoardState(boardName = event.name)
            is BoardOptionsEvent.UpdateBoardData -> boardData = event.boardEntity
        }
    }

    private fun duplicateBoard() {
        isOpenDuplicateDialog = false

        viewModelScope.launch {
            when (val result = duplicateBoardUseCase(boardData)) {
                is DuplicateBoardState.Success -> {
                    showDuplicateSnackbar(result.boardId)
                    closeBoardOptionsDialog()
                }

                else -> {
                    /* TODO What if duplicate fails due to no internet connection? */
                }
            }
        }
    }

    private suspend fun showDuplicateSnackbar(newBoardId: String) {
        snackbarManager.sendSnackbar(
            SnackbarEvent.ShowSnackbar(
                messageResId = R.string.board_successfully_duplicated,
                actionLabelResId = R.string.open_board,
                action = {
                    navigationActions.navigateToBoardItem(newBoardId)
                },
            )
        )
    }

    private fun deleteBoard() {
        isOpenDeleteDialog = false
        val id: String? = boardData.id

        viewModelScope.launch {
            if (id == null) {
                toastManager.sendToast(ToastEvent(R.string.delete_board_app_failure))
            } else {
                val deleteState = deleteBoardUseCase.deleteBoard(id)
                val toastMessageId: Int

                if (deleteState is DeleteBoardState.DeleteSuccess) {
                    toastMessageId = R.string.board_deleted
                    closeBoardOptionsDialog()
                } else {
                    toastMessageId = R.string.delete_board_failure
                }
                toastManager.sendToast(toast = ToastEvent(messageResId = toastMessageId))
            }
        }
    }

    private fun inviteViaEmailToBoard() {
        if (validateInviteViaEmailFields()) {
            viewModelScope.launch {
                boardData.id?.let { boardId ->
                    val userId = when (val getUserResult =
                        getUserUseCase.getUserByEmail(inviteViaEmailState.email)) {
                        is GetUserStateSuccess -> getUserResult.user.id
                        is GetUserStateUserNotFound -> {
                            val createTemporaryUserResult =
                                createUserUseCase.createTemporaryUser(inviteViaEmailState.email)
                            (createTemporaryUserResult as? RegistrationSuccess)?.userId
                        }

                        else -> null
                    }

                    if (userId != null) {
                        addUserToBoardUseCase(
                            boardId = boardId,
                            userId = userId
                        )
                        toastManager.sendToast(ToastEvent(R.string.invite_to_board_app_success))
                        isOpenInviteViaEmailDialog = false
                        closeBoardOptionsDialog()
                    } else {
                        toastManager.sendToast(ToastEvent(R.string.invite_to_board_app_failure))
                    }
                    /*TODO invitationLinkUseCase.sendInvitation(boardData.id)*/
                }?: toastManager.sendToast(ToastEvent(R.string.invite_to_board_app_failure))
            }
        }
    }


    private fun validateInviteViaEmailFields(): Boolean {
        val emailResult = validateEmailUseCase(inviteViaEmailState.email)
        val emailValidation = ValidationMapper.toValidationState(emailResult)
        inviteViaEmailState = inviteViaEmailState.copy(
            isEmailError = emailValidation.isError,
            emailErrorTextId = emailValidation.errorMessageId,
        )
        return !emailValidation.isError
    }

    private fun updateInviteViaEmailState(email: String) {
        inviteViaEmailState = inviteViaEmailState.copy(
            email = email
        )
    }

    private fun updateRenameBoardState(boardName: String) {
        if (validateRenameBoardFields(boardName)) {
            val charactersLeft = boardNameValidationRules.maxLength - boardName.length

            val errorMessageResource = if (isApproachingCharacterLimit(charactersLeft)) {
                StringResource(R.string.characters_left, charactersLeft)
            } else {
                null
            }

            boardNameState = boardNameState.copy(
                boardNameText = boardName,
                boardNameIsError = false,
                boardNameErrorTextResource = errorMessageResource
            )
        }
    }

    private fun renameBoard() {
        viewModelScope.launch {
            val newBoardName = boardNameState.boardNameText

            if (validateRenameBoardFields(newBoardName) && checkNameExists(newBoardName)) {
                val boardNameResult = renameBoardUseCase.renameBoard(boardData, newBoardName)
                if (boardNameResult is RenameBoardState.RenameBoardSuccess) {
                    isOpenRenameDialog = false
                    closeBoardOptionsDialog()
                } else {
                    val messageId = boardNameResult.toMessageId()
                    if (messageId != null) {
                        toastManager.sendToast(ToastEvent(messageId, Toast.LENGTH_LONG))
                    }
                }
            }
        }
    }

    private fun validateRenameBoardFields(newBoardName: String): Boolean {
        val nameResult = validateBoardNameUseCase(newBoardName)
        val nameValidation = ValidationMapper.toValidationState(nameResult)

        val errorMessageResource = nameValidation.errorMessageId?.let {
            when (nameResult) {
                is FieldValidationResult.TooManyCharacters -> {
                    StringResource(
                        nameValidation.errorMessageId,
                        newBoardName.length,
                        boardNameValidationRules.maxLength
                    )
                }

                else -> {
                    StringResource(nameValidation.errorMessageId)
                }
            }
        }

        boardNameState = boardNameState.copy(
            boardNameText = newBoardName,
            boardNameIsError = nameValidation.isError,
            boardNameErrorTextResource = errorMessageResource,
        )
        return !nameValidation.isError
    }

    private suspend fun checkNameExists(newName: String): Boolean {
        val isNameExists = checkBoardNameExistsUseCase.checkBoardNameExists(newName)
        val isNameExistsValidation = ValidationMapper.toValidationState(isNameExists)

        val errorMessageResource = isNameExistsValidation.errorMessageId?.let {
            StringResource(isNameExistsValidation.errorMessageId)
        }

        withContext(Dispatchers.Main) {
            boardNameState = boardNameState.copy(
                boardNameIsError = isNameExistsValidation.isError,
                boardNameErrorTextResource = errorMessageResource,
            )
        }

        return !isNameExistsValidation.isError
    }

    private fun isApproachingCharacterLimit(charactersLeft: Int): Boolean {
        return charactersLeft <= CHARACTERS_LEFT_WARNING_THRESHOLD
    }

    private fun openRenameBoardDialog() {
        isOpenRenameDialog = true
        boardNameState = RenameBoardUIState(boardNameText = boardData.title)
    }

    private fun closeInviteViaEmailDialog() {
        isOpenInviteViaEmailDialog = false
        inviteViaEmailState = InviteViaEmailToBoardUIState()
    }

    private suspend fun closeBoardOptionsDialog() {
        _callbackEventFlow.emit(BoardOptionsDismissEvent)
    }

    companion object {
        val KEY: String = BoardOptionsViewModel::class.java.name
    }
}

@Composable
fun getViewModel(data: BoardEntity): BoardOptionsViewModel {
    return viewModel(key = KEY + data.hashCode(), args = data)
}
