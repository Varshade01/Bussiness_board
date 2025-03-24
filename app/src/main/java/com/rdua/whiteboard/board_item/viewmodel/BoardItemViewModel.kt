package com.rdua.whiteboard.board_item.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.action.ChangeLayerAction
import com.rdua.whiteboard.board.composable.utils.LineData
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.manager.BoardManager
import com.rdua.whiteboard.board.model.BoardTextItemModel
import com.rdua.whiteboard.board.model.ShapeType
import com.rdua.whiteboard.board_item.event.BoardEvent
import com.rdua.whiteboard.board_item.event.BoardItemEvent
import com.rdua.whiteboard.board_item.event.BoardToolBarEvent
import com.rdua.whiteboard.board_item.event.TextToolbarEvent
import com.rdua.whiteboard.board_item.event.ThreeDotsEvent
import com.rdua.whiteboard.board_item.navigation.IBoardItemNavigationActions
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.FontSizeActions
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.TextStyleState
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.toTextStyle
import com.rdua.whiteboard.board_item.state.BoardItemUIState
import com.rdua.whiteboard.board_item.usecase.IGetBoardItemInfoUseCase
import com.rdua.whiteboard.board_item.utils.deleteDecimalZeroInFontSize
import com.rdua.whiteboard.board_item.utils.removeExtraDots
import com.rdua.whiteboard.board_item.utils.takeNotCountingDots
import com.rdua.whiteboard.common.manager.board.BoardSubscriptionManager
import com.rdua.whiteboard.common.manager.snackbar.SnackbarManager
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.usecases.GetDeviceIdUseCase
import com.rdua.whiteboard.common.usecases.IGetUserUseCase
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.data.entities.boards.BoardEntityState
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.navigation.destinations.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val FONT_SIZE_FIELD_CHARACTER_LIMIT = 4

@HiltViewModel
class BoardItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserUseCase: IGetUserUseCase,
    private val getDeviceIdUseCase: GetDeviceIdUseCase,
    private val getBoardItemInfoUseCase: IGetBoardItemInfoUseCase,
    private val navigationActions: IBoardItemNavigationActions,
    private val boardSubscriptionManager: BoardSubscriptionManager,
    private val boardManager: BoardManager,
    private val toastManager: ToastManager,
    snackbarManager: SnackbarManager,
) : ViewModel() {

    val snackbarFlow = snackbarManager.snackbarFlow

    val boardId: String?
    val boardEntityState: StateFlow<BoardEntityState?>

    var state by mutableStateOf(BoardItemUIState())
        private set

    val frameBorderColors: List<Color> get() = boardManager.frameBorderColors
    val shapeBorderColors: List<Color> get() = boardManager.shapeBorderColors
    val frameColors: List<Color> get() = boardManager.frameColors
    val shapeColors: List<Color> get() = boardManager.shapeColors
    val stickyColors: List<Color> get() = boardManager.stickyColors
    val lineColors: List<Color> get() = boardManager.lineColors
    val shapeFontColors: List<Color> get() = boardManager.shapeFontColors
    val stickyFontColors: List<Color> get() = boardManager.stickyFontColors
    val textFontColors: List<Color> get() = boardManager.textFontColors
    val stickyFontSizes: List<Int> get() = boardManager.stickyFontSizes
    val shapeFontSizes: List<Int> get() = boardManager.shapeFontSizes
    val textFontSizes: List<Int> get() = boardManager.textFontSizes

    val isEnableUndoAction get() = boardManager.isEnableUndoActionState
    val isEnableRedoAction get() = boardManager.isEnableRedoActionState

    val boardItems = boardManager.items
    var selectedItem = boardManager.selectedItem


    init {
        // boardId will be passed correctly when navigating to this board screen.
        boardId = savedStateHandle.get<String>(Destination.BoardScreen.BOARD_ID_KEY)
        boardEntityState = boardSubscriptionManager.subscribeTo(boardId)

        viewModelScope.launch {
            launch {
                if (boardId != null) {
                    initializeBoard(boardId)
                    boardEntityState.collectAndUpdateBoardState()
                } else {
                    navigateBackWithErrorToast()
                }
            }
            // Temporary solution to close additional toolbars when other user blocks currently
            // selected item.
            launch {
                selectedItem.collect {
                    if (it == null) {
                        resetAdditionalToolbars()
                        state = state.copy(isOpenThreeDotsToolbar = false)
                    } else if (it is BoardTextItemModel) {
                        // On each change of selected item, if it's a text item, update font value
                        // displayed in TextToolbar.
                        updateFontSizeState(it.textStyle.fontSize.value.toString())
                    }
                }
            }
        }
    }

    override fun onCleared() {
        boardSubscriptionManager.unsubscribeFrom(boardId)
        boardManager.onFinish()
        super.onCleared()
    }

    fun onEvent(event: BoardItemEvent) {
        when (event) {
            is BoardItemEvent.StickyEvent -> addStickyNote()
            is BoardItemEvent.TextEvent -> addText()
            is BoardItemEvent.ShapesEvent -> state = state.copy(isOpenShapesBottomSheet = true)
            is BoardItemEvent.ImageEvent -> {}
            is BoardItemEvent.CreateFrameEvent -> addFrame()
            is BoardItemEvent.AddCommentEvent -> {}
            is BoardItemEvent.NavigateBackEvent -> navigateBack()
            is BoardItemEvent.ShareEvent -> {}
            is BoardItemEvent.MicrophoneEvent -> {}
            is BoardItemEvent.SearchTextChangeEvent -> searchTextChange(event.searchText)
            is BoardItemEvent.CloseBottomSheetShape -> state = state.copy(isOpenShapesBottomSheet = false)
            is BoardItemEvent.Select -> selectItem(event.id)
            is BoardItemEvent.EnableEditMode -> enableEditMode(event.id)
            is BoardItemEvent.DisableEditMode -> boardManager.disableEditMode(event.id)
            is BoardItemEvent.DisableAutoTextWidth -> disableAutoTextWidth(event.id)
            is BoardItemEvent.PositionChange -> moveItem(event.id, event.offset)
            is BoardItemEvent.TextChange -> textChange(event.id, event.newText)
            is BoardItemEvent.SizeChange -> sizeChange(event.id, event.size, event.offset)
            is BoardItemEvent.ScaleChange -> boardManager.scale(event.id, event.scale, event.offset)
            is BoardItemEvent.TextScaleChange -> fontScaleChange(event.id, event.newFont, event.offset)
            is BoardItemEvent.TextSizeChange -> boardManager.resizeText(event.id, event.size)
            is BoardItemEvent.FontSizeChange -> fontSizeChange(event.id, event.newFont)
            is BoardItemEvent.EndChangePosition -> endMove(event.id)
            is BoardItemEvent.UndoAction -> undoAction()
            is BoardItemEvent.RedoAction -> redoAction()
            is BoardItemEvent.EndResize -> boardManager.resizeEnd(event.id)
            is BoardItemEvent.DismissSelectBoardItem -> dismissSelectBoardItem()
            is BoardItemEvent.SetMaxFontSize -> updateMaxFontSize(event.id, event.maxSize)
            is BoardItemEvent.AutoFontSizeModeChange -> updateAutoFontSizeMode(event.id, event.isEnabled)
            is BoardItemEvent.ResizeLine -> resizeLine(event.lineData)
        }
    }

    fun onEvent(event: BoardToolBarEvent) {
        when (event) {
            is BoardToolBarEvent.OpenBorderColorBar -> toggleBorderColorBar()
            is BoardToolBarEvent.SelectedBorderColor -> changeBorderColor(event.color)
            is BoardToolBarEvent.CopyBoardItem -> copyBoardItem()
            is BoardToolBarEvent.OpenBackgroundColorBar -> toggleBackgroundColorBar()
            is BoardToolBarEvent.SelectedBackgroundColor -> changeBackgroundColor(event.color)
            is BoardToolBarEvent.DeleteBoardItem -> deleteBoardElement()
        }
    }

    fun onEvent(event: TextToolbarEvent) {
        when (event) {
            is TextToolbarEvent.TextStyleChange -> changeTextStyle(event.styleState)
            is TextToolbarEvent.ScaleDownText -> changeScaledFontSize(FontSizeActions.SCALE_DOWN)
            is TextToolbarEvent.ScaleUpText -> changeScaledFontSize(FontSizeActions.SCALE_UP)
            is TextToolbarEvent.HorizontalTextAlign -> changeHorizontalTextAlign(textAlign = event.textAlign)
            is TextToolbarEvent.VerticalTextAlignment -> changeVerticalTextAlignment(alignment = event.alignment)
            is TextToolbarEvent.ToggleTextToolbar -> toggleTextToolbar()
            is TextToolbarEvent.ToggleFontColorBar -> toggleFontColorBar()
            is TextToolbarEvent.SelectedFontColor -> changeFontColor(color = event.color)
            is TextToolbarEvent.ToggleFontSizeToolbar -> state = state.copy(isOpenFontSizeToolbar = true)
            is TextToolbarEvent.SetAutoMode -> updateAutoFontSizeMode(event.switchAutoMode)
            is TextToolbarEvent.EnterFontSize -> enterFontSize(event.fontSize)
            is TextToolbarEvent.ChangeFontSize -> fontSizeChange(event.fontSize)
            is TextToolbarEvent.UpdateFontSizeText -> updateFontSizeText()
        }
    }

    fun onEvent(event: ThreeDotsEvent) {
        when (event) {
            is ThreeDotsEvent.CopyLink -> {}
            is ThreeDotsEvent.Lock -> lockBoardItem(id = event.id)
            is ThreeDotsEvent.Unlock -> unlockBoardItem(id = event.id)
            is ThreeDotsEvent.CreateFrame -> createOuterFrame(innerItemId = event.id)
            is ThreeDotsEvent.ShowInfo -> showBoardItemInfo()
            is ThreeDotsEvent.HideInfo -> state = state.copy(isOpenBoardItemInfoBar = false)
            is ThreeDotsEvent.BringFront -> bringToFrontBoardItem(event.id)
            is ThreeDotsEvent.SendBack -> sendToBackBoardItem(event.id)
            is ThreeDotsEvent.OpenToolbar -> openThreeDotsToolbar()
            is ThreeDotsEvent.CloseToolbar -> state = state.copy(isOpenThreeDotsToolbar = false)
        }
    }

    fun onEvent(event: BoardEvent) {
        when(event) {
            is BoardEvent.OpenBoardOptions -> state = state.copy(isOpenBoardOptions = true)
            is BoardEvent.CloseBoardOptions -> state = state.copy(isOpenBoardOptions = false)
        }
    }

    /**
     * Invokes the resizeLine BoardManager function.
     */
    private fun resizeLine(lineData: LineData?) {
        boardManager.resizeLine(lineData)
    }

    /**
     * Changes auto font size mode for the board item.
     *
     * Is invoked by the TextToolbar.
     */
    private fun updateAutoFontSizeMode(isEnabled: Boolean) {
        selectedItem.value?.id?.let { id ->
            boardManager.changeAutoFontSizeMode(id, isEnabled)
        }
    }

    /**
     * Changes auto font size mode for the board item.
     *
     * Is invoked by the board item.
     */
    private fun updateAutoFontSizeMode(id: String, isEnabled: Boolean) {
        boardManager.changeAutoFontSizeMode(id, isEnabled)
    }

    /**
     * Updates the font size state when user closes keyboard after editing font in font text field
     * in TextToolbar.
     */
    private fun updateFontSizeText() {
        (selectedItem.value as? BoardTextItemModel)?.let { selectedItem ->
            updateFontSizeState(selectedItem.textStyle.fontSize.value.toString())
        }
    }

    /**
     * Updates the font size state to [size].
     *
     * If the passed [size] is longer than 4 characters, truncates it to the first 4 characters.
     *
     * @param size The font size to update the state.
     */
    private fun updateFontSizeState(size: String) {
        state = state.copy(fontSizeInput = size.deleteDecimalZeroInFontSize())
    }

    /**
     * This method is invoked when the user scales the TextUI, allowing for adjustments
     * in font size and layout based on the provided parameters.
     */
    private fun fontScaleChange(id: String, newFont: TextUnit, offset: DpOffset) {
        boardManager.scaleTextUI(id, newFont, offset)
    }

    /**
     * Updates items font.
     *
     * Is invoked when font size is changed by the board item Auto font size mode.
     */
    private fun fontSizeChange(id: String, newFont: TextUnit) {
        boardManager.updateFontSize(id, newFont)
    }

    /**
     * Updates items font. As a standalone operation, should save change in undo/redo backstack
     * and remote.
     *
     * Is invoked when font size is selected on TextToolbar.
     */
    private fun fontSizeChange(newFont: TextUnit) {
        val currentFontSize = (selectedItem.value as BoardTextItemModel).textStyle.fontSize

        if (newFont != currentFontSize) {
            boardManager.changeFontSize(newFont)
        }
    }

    /**

     * This function is responsible for processing user input to set the font size of a text item.

     * It takes an input string and performs the following steps:
     * 1. Replaces any commas with dots to handle decimal points uniformly.
     * 2. Removes all characters except numbers and dots.
     * 3. Ensures only the one dot remains in the input.
     * 4. Limits the input to a maximum of 5 characters, including the dot.
     * 5. Deletes any trailing zero after the decimal point.
     *
     * After processing the input, it calculates the new font size based on the corrected input and ensures
     * it falls within the defined range of minimum and maximum font sizes.

    @param input The input string representing the desired font size.
     */
    private fun enterFontSize(input: String) {
        val currentItem = (selectedItem.value as? BoardTextItemModel) ?: return

        val correctedInput = input
            .replace(",", ".")
            .removeExtraDots()
            .replace("[^\\d.]".toRegex(), "")
            .takeNotCountingDots(FONT_SIZE_FIELD_CHARACTER_LIMIT)
            .deleteDecimalZeroInFontSize()

        state = state.copy(fontSizeInput = correctedInput)

        // Use min size for "empty" input. Otherwise correct withing min..max range.
        val newFontSize = when {
            correctedInput.isBlank() || correctedInput == "." -> DefaultTextStyles.minFontSize
            else -> {
                correctedInput.toFloat().coerceIn(
                    minimumValue = DefaultTextStyles.minFontSize.value,
                    maximumValue = currentItem.maxFontSize.value,
                ).sp
            }
        }

        if (newFontSize != currentItem.textStyle.fontSize) {
            boardManager.changeFontSize(newFontSize)
        }
    }

    private fun updateMaxFontSize(id: String, maxSize: TextUnit) {
        boardManager.updateMaxFontSize(id, maxSize)
    }

    private fun changeScaledFontSize(fontSizeActions: FontSizeActions) {
        boardManager.changeFontSize(fontSizeActions)
    }

    fun addShape(shape: ShapeType) {
        state = state.copy(isOpenShapesBottomSheet = false)

        when (shape) {
            ShapeType.LINE -> boardManager.addLine()
            else -> boardManager.addShape(type = shape)
        }
    }

    private fun addStickyNote() {
        boardManager.addStickyNote()
    }

    private fun addText() {
        boardManager.addText()
    }

    private fun addFrame() {
        boardManager.addFrame()
    }

    private fun createOuterFrame(innerItemId: String) {
        state = state.copy(isOpenThreeDotsToolbar = false)
        boardManager.createOuterFrame(innerItemId = innerItemId)
    }

    private fun deleteBoardElement() {
        resetAdditionalToolbars()
        boardManager.deleteBoardElement()
    }

    private fun copyBoardItem() {
        resetAdditionalToolbars()
        boardManager.copyBoardItem()
    }

    private fun lockBoardItem(id: String) {
        state = state.copy(isOpenThreeDotsToolbar = false)
        boardManager.toggleLockBoardItem(id = id, isLock = true)
    }

    private fun unlockBoardItem(id: String) {
        state = state.copy(isOpenThreeDotsToolbar = false)
        boardManager.toggleLockBoardItem(id = id, isLock = false)
    }

    private fun showBoardItemInfo() {
        state = state.copy(isOpenThreeDotsToolbar = false)
        selectedItem.value?.let { selectedItem ->
            viewModelScope.launch {
                state = state.copy(
                    selectedItemInfo = getBoardItemInfoUseCase(selectedItem),
                    isOpenBoardItemInfoBar = true
                )
            }
        }
    }

    private fun bringToFrontBoardItem(id: String) {
        state = state.copy(isOpenThreeDotsToolbar = false)
        boardManager.changeBoardItemLayer(id = id, action = ChangeLayerAction.BringToFront)
    }

    private fun sendToBackBoardItem(id: String) {
        state = state.copy(isOpenThreeDotsToolbar = false)
        boardManager.changeBoardItemLayer(id = id, action = ChangeLayerAction.SendToBack)
    }

    private fun openThreeDotsToolbar() {
        resetAdditionalToolbars()
        state = state.copy(isOpenThreeDotsToolbar = true)
    }

    private fun toggleBorderColorBar() {
        resetAdditionalToolbars(isOpenBorderColorBar = !state.isOpenBorderColorBar)
    }

    private fun toggleBackgroundColorBar() {
        resetAdditionalToolbars(isOpenBackgroundColorBar = !state.isOpenBackgroundColorBar)
    }

    private fun toggleFontColorBar() {
        state = state.copy(isOpenFontColorBar = !state.isOpenFontColorBar)
    }

    private fun toggleTextToolbar() {
        resetAdditionalToolbars(isOpenTextToolbar = !state.isOpenTextToolbar)
    }

    private fun undoAction() {
        resetAdditionalToolbars()
        boardManager.undoAction()
    }

    private fun redoAction() {
        resetAdditionalToolbars()
        boardManager.redoAction()
    }

    private fun dismissSelectBoardItem() {
        resetAdditionalToolbars()
        boardManager.deselectBoardItem()
    }

    /**
     * Resets addition toolbars to their default closed state. Provides parameters to manually
     * specify the state of specific toolbars, so it can for example open one toolbar while closing
     * all the others.
     */
    private fun resetAdditionalToolbars(
        isOpenBorderColorBar: Boolean = false,
        isOpenBackgroundColorBar: Boolean = false,
        isOpenFontColorBar: Boolean = false,
        isOpenTextToolbar: Boolean = false,
        isOpenFontSizeToolbar: Boolean = false,
        isOpenBoardItemInfoBar: Boolean = false,
    ) {
        state = state.copy(
            isOpenBorderColorBar = isOpenBorderColorBar,
            isOpenBackgroundColorBar = isOpenBackgroundColorBar,
            isOpenFontColorBar = isOpenFontColorBar,
            isOpenTextToolbar = isOpenTextToolbar,
            isOpenFontSizeToolbar = isOpenFontSizeToolbar,
            isOpenBoardItemInfoBar = isOpenBoardItemInfoBar,
        )
    }

    private fun changeBorderColor(color: Color) {
        state = state.copy(isOpenBorderColorBar = false)
        boardManager.changeBorderColor(newColor = color)
    }

    private fun changeBackgroundColor(color: Color) {
        state = state.copy(isOpenBackgroundColorBar = false)
        boardManager.changeBackgroundColor(newColor = color)
    }

    private fun changeFontColor(color: Color) {
        state = state.copy(isOpenFontColorBar = false)
        boardManager.changeFontColor(newColor = color)
    }

    private fun changeHorizontalTextAlign(textAlign: TextAlign) {
        state = state.copy(isOpenFontColorBar = false)
        boardManager.changeHorizontalTextAlign(textAlign = textAlign)
    }

    private fun changeVerticalTextAlignment(alignment: Alignment) {
        state = state.copy(isOpenFontColorBar = false)
        boardManager.changeVerticalTextAlignment(alignment = alignment)
    }

    private fun changeTextStyle(styleState: TextStyleState) {
        selectedItem.value?.id?.let { id ->
            boardManager.changeTextStyle(id = id, newTextStyle = styleState.toTextStyle())
        }
    }

    private fun selectItem(id: String) {
        resetAdditionalToolbars()
        boardManager.selectItem(id)
    }

    private fun enableEditMode(id: String) {
        resetAdditionalToolbars()
        boardManager.enableEditMode(id)
    }

    private fun moveItem(id: String, offset: DpOffset) {
        if (selectedItem.value?.isLocked == false) {
            boardManager.move(id, offset)
        }
    }

    private fun endMove(id: String) {
        boardManager.endMove(id)
    }

    private fun textChange(id: String, newText: String) {
        boardManager.changeText(id, newText)
    }

    private fun sizeChange(id: String, size: DpSize, offset: DpOffset) {
        boardManager.resize(id, size, offset)
    }

    private fun disableAutoTextWidth(id: String) {
        boardManager.disableAutoTextWidth(id)
    }

    private fun searchTextChange(searchText: String) {
        state = state.copy(searchText = searchText)
    }

    private suspend fun initializeBoard(boardId: String) {
        // If has Internet connection
        initializeBoardFromRemote(boardId)
    }

    private suspend fun initializeBoardFromRemote(boardId: String) {
        val deviceId = getDeviceIdUseCase()
        val result = getUserUseCase.getCurrentUser()

        if (result is GetUserStateSuccess) {
            boardManager.init(boardId, result.user.id, result.user.name, deviceId, viewModelScope)
        } else {
            navigateBackWithErrorToast()
        }
    }

    private suspend fun StateFlow<BoardEntityState?>.collectAndUpdateBoardState() {
        collect { boardEntity ->
            if (boardEntity == null) {
                navigateBackWithBoardDeletedToast()
            } else if (boardEntity !is BoardEntityState.Loading) {
                boardManager.updateBoardFromRemote(boardEntity as BoardEntity)
            }
        }
    }

    private suspend fun navigateBackWithBoardDeletedToast() {
        navigateBackWithToast(R.string.board_deleted)
    }

    private suspend fun navigateBackWithErrorToast() {
        navigateBackWithToast(R.string.could_not_load_board)
    }

    private suspend fun navigateBackWithToast(messageResId: Int) {
        toastManager.sendToast(ToastEvent(messageResId))
        navigateBack()
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
}
