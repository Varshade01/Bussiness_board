package com.rdua.whiteboard.board.manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.board.action.ChangeLayerAction
import com.rdua.whiteboard.board.composable.utils.LineData
import com.rdua.whiteboard.board.composable.utils.calculateOffsetWithRotation
import com.rdua.whiteboard.board.constants.DefaultColors
import com.rdua.whiteboard.board.constants.DefaultSizes
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.data.BlockingToken
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.manager.operations.BoardItemOperation
import com.rdua.whiteboard.board.manager.operations.beginOperation
import com.rdua.whiteboard.board.manager.utils.calculateFrameCoordinate
import com.rdua.whiteboard.board.manager.utils.calculateFrameSize
import com.rdua.whiteboard.board.manager.utils.checkItemWithinBounds
import com.rdua.whiteboard.board.manager.utils.isItemOutsideFrame
import com.rdua.whiteboard.board.model.BoardItemInnerModel
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.BoardScalableItemModel
import com.rdua.whiteboard.board.model.BoardTextItemAutoFontModel
import com.rdua.whiteboard.board.model.BoardTextItemModel
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.board.model.LineModel
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.ShapeType
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.board.model.ValueModel
import com.rdua.whiteboard.board.usecase.CheckBlockTokenValidityUseCase
import com.rdua.whiteboard.board.usecase.DeleteBoardItemUseCase
import com.rdua.whiteboard.board.usecase.GetUserInfoUseCase
import com.rdua.whiteboard.board.usecase.SaveBoardItemUseCase
import com.rdua.whiteboard.board.usecase.SaveBoardStateUseCase
import com.rdua.whiteboard.board.utils.swapList
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.FontSizeActions
import com.rdua.whiteboard.common.time.Timestamp
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.di.IoCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class BoardManager @Inject constructor(
    @IoCoroutineScope val coroutineScope: CoroutineScope,
    private val saveBoardItemUseCase: SaveBoardItemUseCase,
    private val saveBoardStateUseCase: SaveBoardStateUseCase,
    private val deleteBoardItemUseCase: DeleteBoardItemUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val checkBlockTokenValidityUseCase: CheckBlockTokenValidityUseCase,
) {
    private lateinit var boardInfo: BoardInfo

    private val boardStack: MutableList<List<BoardItemModel>> = mutableListOf()

    private var _isEnableUndoActionState by mutableStateOf<Boolean?>(null)
    private var _isEnableRedoActionState by mutableStateOf<Boolean?>(null)

    var selectedItem = MutableStateFlow<BoardItemModel?>(null)

    val isEnableUndoActionState get() = _isEnableUndoActionState
    val isEnableRedoActionState get() = _isEnableRedoActionState

    private val _items: SnapshotStateList<BoardItemModel> = mutableStateListOf()
    val items: List<BoardItemModel> = _items

    private val _frameColors = DefaultColors.frameColors.toMutableStateList()
    private val _frameBorderColors = DefaultColors.frameBorderColors.toMutableStateList()
    private val _shapeBorderColors = DefaultColors.shapeBorderColors.toMutableStateList()
    private val _shapeColors = DefaultColors.shapeColors.toMutableStateList()
    private val _stickyColors = DefaultColors.stickyColors.toMutableStateList()
    private val _lineColors = DefaultColors.lineColors.toMutableStateList()
    private val _shapeFontColors = DefaultColors.shapeFontColors.toMutableStateList()
    private val _stickyFontColors = DefaultColors.stickyFontColors.toMutableStateList()
    private val _textFontColors = DefaultColors.textFontColors.toMutableStateList()

    private var lastStickyFontSize = DefaultTextStyles.stickyUITextStyle.fontSize
    private var lastShapeFontSize = DefaultTextStyles.shapeUITextStyle.fontSize
    private var lastTextFontSize = DefaultTextStyles.textUITextStyle.fontSize

    val frameBorderColors: List<Color> get() = _frameBorderColors
    val shapeBorderColors: List<Color> get() = _shapeBorderColors
    val frameColors: List<Color> get() = _frameColors
    val shapeColors: List<Color> get() = _shapeColors
    val stickyColors: List<Color> get() = _stickyColors
    val lineColors: List<Color> get() = _lineColors
    val shapeFontColors: List<Color> get() = _shapeFontColors
    val stickyFontColors: List<Color> get() = _stickyFontColors
    val textFontColors: List<Color> get() = _textFontColors

    val stickyFontSizes: List<Int> get() = DefaultTextStyles.stickyFontSizes
    val shapeFontSizes: List<Int> get() = DefaultTextStyles.shapeFontSizes
    val textFontSizes: List<Int> get() = DefaultTextStyles.textFontSizes

    private var boardStackPosition = 0


    // Lifecycle aware scope for jobs, that should cancel when the board is closed.
    private var viewModelScope: CoroutineScope? = null
    private var maintainBlockTokenJob: Job? = null
    private val maintainBlockTimeInterval = 25000L
    private val blockExpirationCheckTimeInterval = 30000L

    /**
     * It is vital to initialize BoardManger before operating it in online mode.
     *
     * @param boardId the id of the board this BoardManger is working with.
     * @param userId the id of the current user using this BoardManager.
     * @param userName the name of the current user using this BoardManager.
     * @param deviceId the id of this device.
     * @param viewModelScope the scope for the coroutines that should cancel when the board is closed.
     */
    fun init(
        boardId: String,
        userId: String,
        userName: String,
        deviceId: String,
        viewModelScope: CoroutineScope,
    ) {
        boardInfo = BoardInfo(
            boardId = boardId,
            userName = userName,
            userUID = UserUID(
                userId = userId,
                deviceId = deviceId,
            )
        )

        this.viewModelScope = viewModelScope
        launchBlockTokenExpirationChecks()
    }

    /**
     * Perform finishing operations before this BoardManager is stopped.
     *
     * Processes and removes block of a currently blocked item.
     */
    fun onFinish() {
        val indexOfBlocked = _items.indexOfFirst { it.isBlockedByMe() }
        if (indexOfBlocked != -1) {
            var unblockedItem = _items[indexOfBlocked].unblockItem()

            if (isTextActuallyChanged(unblockedItem)) {
                unblockedItem = unblockedItem.updateModifyInfo()
            }
            saveItemChangeToRepository(unblockedItem, indexOfBlocked)
        }
    }

    //////////////////////////////////////////////////
    //             User interactions                //
    //////////////////////////////////////////////////

    fun addStickyNote(
        coordinate: DpOffset = DpOffset(x = 110.dp, y = 160.dp),
    ) {
        val timestamp = Timestamp.now()

        addBoardItem(
            StickyModel(
                id = generateNewID(),
                coordinate = coordinate,
                color = stickyColors[0],
                isSelected = true,
                isEditMode = true,
                creator = boardInfo.userUID.userId,
                createdAt = timestamp,
                modifiedBy = boardInfo.userUID.userId,
                modifiedAt = timestamp,
                author = boardInfo.userName,
                textStyle = DefaultTextStyles.stickyUITextStyle.copy(
                    color = stickyFontColors[0],
                    fontSize = lastStickyFontSize,
                ),
            ).blockItem()
        )
    }

    fun addShape(
        type: ShapeType,
        coordinate: DpOffset = DpOffset(x = 210.dp, y = 100.dp),
    ) {
        val timestamp = Timestamp.now()

        addBoardItem(
            ShapeModel(
                id = generateNewID(),
                type = type,
                coordinate = coordinate,
                color = shapeColors[0],
                borderColor = shapeBorderColors[0],
                isSelected = true,
                creator = boardInfo.userUID.userId,
                createdAt = timestamp,
                modifiedBy = boardInfo.userUID.userId,
                modifiedAt = timestamp,
                textStyle = DefaultTextStyles.shapeUITextStyle.copy(
                    color = shapeFontColors[0],
                    fontSize = lastShapeFontSize,
                ),
            )
        )
    }

    fun addLine(
        coordinate: DpOffset = DpOffset(x = 150.dp, y = 150.dp),
    ) {
        val timestamp = Timestamp.now()
        val endCoordinate = DpOffset(
            x = coordinate.x + DefaultSizes.lineSize.width,
            y = coordinate.y
        )

        addBoardItem(
            boardItem = LineModel(
                id = generateNewID(),
                coordinate = coordinate,
                endCoordinate = endCoordinate,
                rotationAngle = 0f,
                isSelected = true,
                creator = boardInfo.userUID.userId,
                createdAt = timestamp,
                modifiedBy = boardInfo.userUID.userId,
                modifiedAt = timestamp,
                color = lineColors[0],
            )
        )
    }

    fun addText(
        coordinate: DpOffset = DpOffset(x = 100.dp, y = 300.dp),
    ) {
        val timestamp = Timestamp.now()

        addBoardItem(
            TextModel(
                id = generateNewID(),
                coordinate = coordinate,
                isSelected = true,
                isEditMode = true,
                creator = boardInfo.userUID.userId,
                createdAt = timestamp,
                modifiedBy = boardInfo.userUID.userId,
                modifiedAt = timestamp,
                textStyle = DefaultTextStyles.textUITextStyle.copy(
                    color = textFontColors[0],
                    fontSize = lastTextFontSize,
                ),
            ).blockItem()
        )
    }

    fun addFrame(
        coordinate: DpOffset = DpOffset(x = 80.dp, y = 140.dp),
    ) {
        val timestamp = Timestamp.now()

        addBoardItem(
            boardItem = FrameModel(
                id = generateNewID(),
                coordinate = coordinate,
                isSelected = true,
                isEditMode = true,
                creator = boardInfo.userUID.userId,
                createdAt = timestamp,
                modifiedBy = boardInfo.userUID.userId,
                modifiedAt = timestamp,
                color = frameColors[0],
                borderColor = frameBorderColors[0],
            )
        )
    }

    /**
     * Creates an outer Frame for the selected item (selected item is found by id).
     */
    fun createOuterFrame(innerItemId: String) {
        val timestamp = Timestamp.now()
        val innerItem: BoardItemModel? = items.find { innerItemId == it.id }

        innerItem?.let { item ->
            val frameSize: DpSize = calculateFrameSize(item = item)
            val frameCoordinate: DpOffset =
                calculateFrameCoordinate(item = item, frameSize = frameSize)

            val frame = FrameModel(
                id = generateNewID(),
                size = frameSize,
                coordinate = frameCoordinate,
                isSelected = true,
                isEditMode = true,
                creator = boardInfo.userUID.userId,
                createdAt = timestamp,
                modifiedBy = boardInfo.userUID.userId,
                modifiedAt = timestamp,
                color = frameColors[0],
                borderColor = frameBorderColors[0],
            )
            setOuterFrameForItem(outerFrame = frame, innerItem = item)
        }
    }

    /**
     * Sets outer frame to selected item and save changes
     */
    private fun setOuterFrameForItem(
        outerFrame: BoardItemModel,
        innerItem: BoardItemModel,
    ) {
        val isThisItem: (BoardItemModel) -> Boolean = { innerItem.id == it.id }
        val innerItemIndex: Int = _items.indexOf(innerItem)

        _items.beginOperation()
            .find(predicate = isThisItem)
            .replaceItem { item, _ ->
                item.toCopy(
                    ValueModel.Selected(isSelected = false),
                    ValueModel.OuterFrameId(outerFrameId = outerFrame.id)
                ).updateModifyInfo()
            }
            .then { _, items ->
                items.add(index = innerItemIndex, element = outerFrame)
                saveBoardChangeState(items = items)
            }
            .updateSelectedItem(value = outerFrame)
            .finishOperation()
    }

    /**
     * Deletes currently selected board item.
     */
    fun deleteBoardElement() {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        val frame: BoardItemModel? = _items.find { isCurrentlySelected(it) } as? FrameModel
        if (frame != null) {
            items.filter { frame.id == (it as? BoardItemInnerModel)?.outerFrameId }
                .forEach {
                    removeOuterFrame(innerItemId = it.id)
                }
        }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .remove()
            .updateSelectedItem(null)
            .saveItemsState()
            .finishOperation()
    }

    /**
     * Removes outer frame of item
     */
    private fun removeOuterFrame(innerItemId: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { innerItemId == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { innerItem, _ ->
                innerItem.toCopy(
                    ValueModel.OuterFrameId(outerFrameId = null),
                ).updateModifyInfo()
            }
            .finishOperation()
    }

    /**
     * Creates a copy of currently selected board item and adds it to the board. Saves this item to
     * database and board state to backstack.
     */
    fun copyBoardItem() {
        val newId: String = generateNewID()
        val timestamp: Timestamp = Timestamp.now()
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { it.isSelected }
        val defaultDuplicateOffset = 40.dp

        _items.beginOperation()
            .find(isCurrentlySelected)
            .disableSelection()
            .addAll { item, items ->
                if (item == null) return@addAll null

                val newItem: BoardItemModel = item.toCopy(
                    ValueModel.Id(id = newId),
                    ValueModel.Selected(isSelected = true),
                    ValueModel.Offset(
                        coordinate = DpOffset(
                            x = item.coordinate.x + defaultDuplicateOffset,
                            y = item.coordinate.y + defaultDuplicateOffset
                        ),
                        endCoordinate = if (item is LineModel)
                            DpOffset(
                                x = item.endCoordinate.x + defaultDuplicateOffset,
                                y = item.endCoordinate.y + defaultDuplicateOffset
                            )
                        else DpOffset.Zero
                    ),
                    ValueModel.Lock(isLocked = false),
                    ValueModel.Creator(creator = boardInfo.userUID.userId),
                    ValueModel.CreatedAt(timestamp = timestamp),
                ).updateModifyInfo()

                items.add(newItem)

                if (item is FrameModel) {
                    val innerItems: List<BoardItemModel> =
                        items.filter { item.id == (it as? BoardItemInnerModel)?.outerFrameId }

                    innerItems.forEach {
                        val copyInnerItem = it.toCopy(
                            ValueModel.Id(id = generateNewID()),
                            ValueModel.Offset(
                                coordinate = DpOffset(
                                    x = it.coordinate.x + defaultDuplicateOffset,
                                    y = it.coordinate.y + defaultDuplicateOffset
                                ),
                                endCoordinate = if (it is LineModel)
                                    DpOffset(
                                        x = it.endCoordinate.x + defaultDuplicateOffset,
                                        y = it.endCoordinate.y + defaultDuplicateOffset
                                    )
                                else DpOffset.Zero
                            ),
                            ValueModel.OuterFrameId(outerFrameId = newId),
                            ValueModel.Creator(creator = boardInfo.userUID.userId),
                            ValueModel.CreatedAt(timestamp = timestamp),
                        ).updateModifyInfo()

                        items.add(copyInnerItem)
                    }
                }
                return@addAll Pair(newItem, items)
            }
            .saveItemsState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Removes selection of currently selected board item. Usually called when user clicks on the board.
     *
     * If item was in EditMode, removes its block and saves its text state if it changed relatively
     * to its last state.
     */
    fun deselectBoardItem() {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .disableSelection()
            .disableEditMode()
            .saveEditedItemState()
            .updateSelectedItem(null)
            .finishOperation()
    }

    /**
     * Changes background color for board items that support it.
     */
    fun changeBackgroundColor(newColor: Color) {
        val colors: SnapshotStateList<Color>? =
            when (selectedItem.value) {
                is ShapeModel -> _shapeColors
                is StickyModel -> _stickyColors
                is FrameModel -> _frameColors
                is LineModel -> _lineColors
                is TextModel,
                null,
                -> null
            }

        if (colors != null) {
            colors.updateColor(color = newColor)
            changeItemBackgroundColor(newColor)
        }
    }

    /**
     * Changes border color for board items.
     */
    fun changeBorderColor(newColor: Color) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->
                item.toCopy(
                    ValueModel.BorderColor(color = newColor),
                ).updateModifyInfo()
            }
            .saveNewItemState()
            .updateSelectedItem()
            .then { item, _ ->
                if (item is ShapeModel) {
                    _shapeBorderColors.updateColor(color = newColor)
                } else if (item is FrameModel) {
                    _frameBorderColors.updateColor(color = newColor)
                }
            }
            .finishOperation()
    }

    /**
     * Changes text font color for board items.
     */
    fun changeFontColor(newColor: Color) {
        val colors: SnapshotStateList<Color>? =
            when (selectedItem.value) {
                is ShapeModel -> _shapeFontColors
                is StickyModel -> _stickyFontColors
                is TextModel -> _textFontColors
                is FrameModel,
                is LineModel,
                null,
                -> null
            }

        if (colors != null) {
            colors.updateColor(color = newColor)
            changeTextFontColor(newColor)
        }
    }

    /**
     * Changes horizontal text align for board item.
     */
    fun changeHorizontalTextAlign(textAlign: TextAlign) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->
                (item as? BoardTextItemModel)?.toCopy(
                    ValueModel.TextStyle(
                        textStyle = item.textStyle.copy(textAlign = textAlign)
                    )
                )?.updateModifyInfo()
            }
            .saveNewItemState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Changes vertical text alignment for board item.
     */
    fun changeVerticalTextAlignment(alignment: Alignment) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->
                item.toCopy(
                    ValueModel.VerticalTextAlignment(alignment = alignment)
                ).updateModifyInfo()
            }
            .saveNewItemState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Toggles item selection state. If another item is currently selected, removes its
     * selection state and EditMode.
     */
    fun selectItem(id: String) {
        val innerItem = _items.find { it.id == id } as? BoardItemInnerModel
        val frame = _items.find { it.id == innerItem?.outerFrameId } as? FrameModel

        // Adds inability to select an inner item if the outer frame is locked
        val isThisItem: (BoardItemModel) -> Boolean =
            if (frame?.isLocked == true && id != frame.id) {
                { it.id == frame.id }
            } else {
                { it.id == id }
            }

        val isOtherSelected: (BoardItemModel) -> Boolean = { it.isSelected && it.id != id }
        val isOtherEditMode: (BoardItemModel) -> Boolean = { it.isEditMode && it.id != id }

        _items.beginOperation()
            .find(isOtherSelected)
            .disableSelection()
            .find(isOtherEditMode)
            .disableEditMode()
            .saveEditedItemState()
            .find(isThisItem)
            .replaceItem { item, _ ->
                item.toCopy(ValueModel.Selected(isSelected = !item.isSelected))
            }
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Changes the font size according to [fontSizeActions] actions.
     *
     * FontSizeActions.SCALE_UP: Determines if the current font size can be increased by 2. If it is possible, increases by 2.
     * Otherwise, calculates the difference between the current font size and the maximum available value.
     *
     * FontSizeActions.SCALE_DOWN: If the current font is greater than 3, it decreases by 2.
     * If the current font is less than 3, it calculates the difference between the current value and the minimum value
     *
     * @param fontSizeActions Actions defining the font size change (increase/decrease by 2).
     */
    fun changeFontSize(fontSizeActions: FontSizeActions) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->

                (item as? BoardTextItemModel)?.updateModifyInfo()?.toCopy(
                    ValueModel.TextStyle(
                        textStyle = item.textStyle.copy(
                            fontSize = (item.textStyle.fontSize.value.let { curValue: Float ->
                                val changedSize = when (fontSizeActions) {
                                    FontSizeActions.SCALE_UP -> curValue + 2
                                    FontSizeActions.SCALE_DOWN -> curValue - 2
                                }
                                val newSize = changedSize.coerceIn(
                                    DefaultTextStyles.minFontSize.value,
                                    item.maxFontSize.value
                                )
                                newSize.sp
                            })
                        )
                    )
                )
            }
            .updateSelectedItem()
            .saveNewItemState()
            .updateLastFontSize()
            .finishOperation()
    }

    /**
     * Changes font size of a board item that supports text ([BoardTextItemModel]), updates data
     * in remote and saves board state to backstack.
     *
     * A typical use might be when font size changes externally and not by the board item itself.
     * (Ex. user modifies font size via toolbar).
     */
    fun changeFontSize(size: TextUnit) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->
                (item as? BoardTextItemModel)?.updateModifyInfo()?.toCopy(
                    ValueModel.TextStyle(
                        textStyle = item.textStyle.copy(
                            fontSize = size
                        )
                    )
                )
            }
            .updateSelectedItem()
            .saveNewItemState()
            .updateLastFontSize()
            .finishOperation()
    }

    /**
     * Updates font size of a board item that supports text ([BoardTextItemModel]) locally without
     * saving to remote or to backstack.
     *
     * A typical use might be when font size changes due to changes in board item itself and remote
     * synchronization and saving to backstack is handles separately (onResizeEnd etc.)
     * (Ex. typing text in AutoMode, resizing text etc.)
     */
    fun updateFontSize(id: String, fontSize: TextUnit) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItemWithCondition(
                condition = { item, _ ->
                    item is BoardTextItemModel
                }) { item, _ ->
                (item as BoardTextItemModel).toCopy(
                    ValueModel.TextStyle(
                        textStyle = item.textStyle.copy(
                            fontSize = fontSize
                        )
                    )
                )
            }
            .updateSelectedItem()
            .updateLastFontSize()
            .finishOperation()
    }

    /**
     * Updates maximum font size of a [BoardTextItemModel]. Changes are local only.
     *
     * @param id id of a board item.
     * @param maxSize new maximum allowed font size.
     */
    fun updateMaxFontSize(id: String, maxSize: TextUnit) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItemWithCondition(
                condition = { item, _ ->
                    item is BoardTextItemModel
                }) { item, _ ->
                (item as BoardTextItemModel).toCopy(
                    ValueModel.MaxFontSize(maxFontSize = maxSize)
                )
            }
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Updates auto font size mode for items that support it [BoardTextItemAutoFontModel] locally
     * without saving to remote or to backstack.
     *
     * Could be invoked by the board item itself or by toggling auto mode in TextToolbar.
     */
    fun changeAutoFontSizeMode(id: String, isEnabled: Boolean) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItemWithCondition(
                condition = { item, _ ->
                    item is BoardTextItemAutoFontModel
                }) { item, _ ->
                (item as BoardTextItemAutoFontModel).toCopy(
                    ValueModel.AutoFontSize(isAutoFontSizeOn = isEnabled)
                )
            }
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Enables EditMode on a given board item. EditMode blocks this item for other users.
     *
     * If other item was selected, moves selection to this item. If other item was in EditMode,
     * disables its block, processes possible text changes (updates modify info, saves back stack
     * etc.) then saves both items to remote (currently saves all board items, because its more
     * stable).
     */
    fun enableEditMode(id: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { it.id == id }
        val isOtherSelected: (BoardItemModel) -> Boolean = { it.isSelected && it.id != id }
        val isOtherEditMode: (BoardItemModel) -> Boolean = { it.isEditMode && it.id != id }
        var otherEditedItem: BoardItemModel? = null

        _items.beginOperation()
            .find(isOtherSelected)
            .disableSelection()
            .find(isOtherEditMode)
            .disableEditMode()
            .then { item, _ ->
                // This item needs to update its text state and remove block remotely.
                if (item != null) otherEditedItem = item
            }
            .find(isThisItem)
            .replaceItem { item, _ ->
                item.blockItem().toCopy(
                    ValueModel.Selected(isSelected = true),
                    ValueModel.Edit(isEditMode = true),
                )
            }
            .updateSelectedItem()
            .then { item, items ->
                item?.let {
                    when {
                        otherEditedItem == null
                        -> saveItemChangeToRepository(item, items.indexOf(item))

                        isTextActuallyChanged(otherEditedItem!!)
                        -> saveBoardChangeState(items)

                        else -> saveBoardChangeToRepository(items)
                    }
                }
            }
            .finishOperation()
    }

    /**
     * Disables EditMode on a given board item, unblocking it for other users.
     *
     * If text was changed, saves change to back stack.
     */
    fun disableEditMode(id: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .disableEditMode()
            .saveEditedItemState()
            .finishOperation()
    }

    /**
     * Disables auto text width mode for TextUI, after it was manually resized. This property will
     * be saved to backstack and database after using stops resizing.
     *
     * Called only once, when user starts resizing TextUI that has AutoTextWidth on.
     */
    fun disableAutoTextWidth(id: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, _ ->
                item.toCopy(ValueModel.AutoTextWidth(isAutoWidthOn = false))
            }
            .finishOperation()
    }

    /**
     * Moves item on the board. Blocks this item for other users unless it was blocked already.
     * New position will be saved to backstack and database only when user finished moving item.
     */
    fun move(id: String, offset: DpOffset) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, items ->

                val newItem: BoardItemModel =
                    if (item is LineModel) {
                        val offsetWithRotation: DpOffset = calculateOffsetWithRotation(
                            rotationAngle = item.rotationAngle.toDouble(),
                            offset = offset
                        )
                        item.toCopy(
                            ValueModel.Offset(
                                coordinate = item.coordinate.plus(offsetWithRotation),
                                endCoordinate = item.endCoordinate.plus(offsetWithRotation)
                            )
                        )
                    } else {
                        item.toCopy(
                            ValueModel.Offset(
                                coordinate = item.coordinate.plus(offset),
                            )
                        )
                    }

                val frame = item as? FrameModel
                if (frame != null) {
                    items.forEachIndexed { index, model ->
                        if ((model as? BoardItemInnerModel)?.outerFrameId == frame.id) {
                            items[index] = model.toCopy(
                                ValueModel.Offset(
                                    coordinate = model.coordinate.plus(offset),
                                    endCoordinate = if (model is LineModel) model.endCoordinate.plus(
                                        offset
                                    )
                                    else DpOffset.Zero
                                )
                            )
                        }
                    }
                }

                if (!newItem.isBlockedByMe()) {
                    newItem.blockItem().also { blockedItem ->
                        saveItemChangeToRepository(
                            item = blockedItem,
                            position = items.indexOfFirst { it.id == blockedItem.id })
                    }
                } else newItem
            }
            .finishOperation()
    }

    /**
     * Called when user stops moving a board item. Unblocks this item unless in EditMode, saves new
     * position to backstack and database.
     */
    fun endMove(id: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, items ->
                val newItem: BoardItemModel = changeItemPosition(item = item, items = items)

                if (newItem.isEditMode) newItem
                else newItem.unblockItem()
            }
            .saveNewItemState()
            .finishOperation()
    }

    /**
     * Function for processing the position of the selected item relative to other board items
     */
    private fun changeItemPosition(
        item: BoardItemModel,
        items: List<BoardItemModel>,
    ): BoardItemModel {
        var tempItem: BoardItemModel = item.updateModifyInfo()

        // Frame can`t be inside another frame
        if (item is FrameModel) return tempItem

        // Checking if the inner item is outside the outer frame
        if ((item as? BoardItemInnerModel)?.outerFrameId != null) {
            val outerFrame: FrameModel? = items.find { item.outerFrameId == it.id } as? FrameModel

            val hasItemInsideFrame: Boolean =
                isItemOutsideFrame(item = item, outerFrame = outerFrame)

            if (!hasItemInsideFrame) return tempItem

            tempItem = tempItem.toCopy(
                ValueModel.OuterFrameId(outerFrameId = null),
            )
        }

        // Checking if item is included in any frame and return outer frame id or null.
        getOuterFrameId(item = tempItem, items = items)?.let {
            tempItem = tempItem.toCopy(
                ValueModel.OuterFrameId(outerFrameId = it)
            )
        }
        return tempItem
    }

    /**
     * Checks if item is inside in any frame and return outer frame id or null.
     */
    private fun getOuterFrameId(
        item: BoardItemModel,
        items: List<BoardItemModel>,
    ): String? {
        for (model: BoardItemModel in items.reversed()) {
            if (model is FrameModel && checkItemWithinBounds(item = item, frame = model)) {
                return model.id
            }
        }
        return null
    }

    /**
     * Resizes board item with new [size]. [offset] is used to move the item for directional resize.
     * By default (with zero [offset]) resizing is done relative to the origin point (top left).
     *
     * If item isn't blocked (by EditMode), blocks it for other users until resizing ends.
     * New position will be saved to backstack and database only when user finished moving item.
     */
    fun resize(id: String, size: DpSize, offset: DpOffset) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, items ->
                val newItem = item.toCopy(
                    ValueModel.Size(size = size),
                    ValueModel.Offset(coordinate = item.coordinate.plus(offset)),
                )

                if (!newItem.isBlockedByMe()) {
                    newItem.blockItem().also { blockedItem ->
                        saveItemChangeToRepository(
                            blockedItem,
                            items.indexOfFirst { it.id == blockedItem.id })
                    }
                } else newItem
            }
            .finishOperation()
    }

    /**
     * Responsible for resizing a [LineModel] on a board based on the provided [LineData]
     */
    fun resizeLine(lineData: LineData?) {
        val isThisItem: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, items ->
                if (item !is LineModel) return@replaceItem item

                val newItem: BoardItemModel = lineData?.let { data ->
                    item.toCopy(
                        ValueModel.Offset(
                            coordinate = data.startCoordinate,
                            endCoordinate = data.endCoordinate
                        ),
                        ValueModel.Size(size = data.size),
                        ValueModel.Rotation(angle = data.rotationAngle),
                    )
                } ?: item.toCopy()

                if (!newItem.isBlockedByMe()) {
                    newItem.blockItem().also { blockedItem ->
                        saveItemChangeToRepository(
                            blockedItem,
                            items.indexOfFirst { it.id == blockedItem.id })
                    }
                } else newItem
            }
            .finishOperation()
    }

    /**
     * Called when user stops manually resizing a board item. Is called when user stops dragging items'
     * handles either for resizing or scaling.
     * Persistently saves item's new size and unblocks it unless its in EditMode. If it's in EditMode,
     * it should be unblocked when EditMode will be disabled.
     */
    fun resizeEnd(id: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, _ ->
                val newItem = if (!item.isEditMode) item.unblockItem() else item
                newItem.updateModifyInfo()
            }
            .saveNewItemState()
            .finishOperation()
    }

    /**
     * Resizes board item when it's size changes due to adding/removing text. Currently supported by
     * TextUI only.
     */
    fun resizeText(id: String, size: DpSize) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, _ ->
                item.toCopy(ValueModel.Size(size = size))
            }
            .finishOperation()
    }

    /**
     * Called when the board item is scaled by dragging its handles. Offset is passed when the item
     * is scaled in a certain direction (ex. when scaling from top-start to bottom-end x-coordinate
     * changes).
     *
     * Item is blocked while scaling is in process.
     */
    fun scale(id: String, scale: Float, offset: DpOffset) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItemWithCondition(
                condition = { item, _ ->
                    item is BoardScalableItemModel
                }) { item, _ ->
                val newItem = item.toCopy(
                    ValueModel.Scale(scale = (item as BoardScalableItemModel).scale + scale),
                    ValueModel.Offset(coordinate = item.coordinate.plus(offset))
                )

                if (!newItem.isBlockedByMe()) {
                    newItem.blockItem().also { blockedItem ->
                        saveItemChangeToRepository(
                            item = blockedItem,
                            position = items.indexOfFirst { it.id == blockedItem.id }
                        )
                    }
                } else newItem
            }
            .finishOperation()
    }

    /**
     * Called when TextUI element is scaled.
     *
     * Currently TextUI is scaled by changing font size instead of a flat scale factor. Offset is passed
     * to move the UI item during directional scaling.
     *
     * Item is blocked while scaling is in process.
     */
    fun scaleTextUI(id: String, newFontSize: TextUnit, offset: DpOffset) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItemWithCondition(
                condition = { item, _ ->
                    item is BoardTextItemModel
                }) { item, _ ->
                val newItem = item.toCopy(
                    ValueModel.TextStyle(
                        textStyle = (item as BoardTextItemModel).textStyle.copy(
                            fontSize = newFontSize
                        )
                    ),
                    ValueModel.Offset(coordinate = item.coordinate.plus(offset))
                )

                if (!newItem.isBlockedByMe()) {
                    newItem.blockItem().also { blockedItem ->
                        saveItemChangeToRepository(
                            item = blockedItem,
                            position = items.indexOfFirst { it.id == blockedItem.id }
                        )
                    }
                } else newItem
            }
            .updateSelectedItem()
            .updateLastFontSize()
            .finishOperation()
    }

    /**
     * Called when user modifies text on board items that support it. Updates local UI state only.
     * New text will be saved to backstack and database only when user toggles EditMode.
     */
    fun changeText(id: String, newText: String) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItem { item, _ ->
                item.toCopy(ValueModel.Text(text = newText))
            }
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Toggle item lock mode. During lock mode item cannot be edited. Lock operations are saved to
     * backstack and database.
     */
    fun toggleLockBoardItem(id: String, isLock: Boolean) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .disableEditMode()
            .replaceItem { item, _ ->
                item.toCopy(ValueModel.Lock(isLocked = isLock))
                    .updateModifyInfo()
            }
            .updateSelectedItem()
            .then { item, items ->
                if (item is FrameModel) {

                    items.forEachIndexed { index, boardItem ->
                        if ((boardItem as? BoardItemInnerModel)?.outerFrameId == item.id) {
                            items[index] = boardItem
                                .toCopy(ValueModel.Lock(isLocked = isLock))
                                .updateModifyInfo()
                        }
                    }
                }
            }
            .saveItemsState()
            .finishOperation()
    }

    /**
     * Changes item's drawing order to either be drawn on top of other items or below them on z axis.
     * Saves new board drawing order to backstack and database.
     */
    fun changeBoardItemLayer(id: String, action: ChangeLayerAction) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .remove()
            .then { item, items ->
                if (item == null) return@then
                val updatedItem: BoardItemModel = item.updateModifyInfo()

                when (action) {
                    is ChangeLayerAction.BringToFront -> items.add(updatedItem)
                    is ChangeLayerAction.SendToBack -> items.add(0, updatedItem)
                }
                saveBoardChangeState(items)
            }
            .finishOperation()
    }

    /**
     * Handles background color changes in board item list.
     */
    private fun changeItemBackgroundColor(newColor: Color) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->
                item.toCopy(
                    ValueModel.BackgroundColor(color = newColor),
                ).updateModifyInfo()
            }
            .saveNewItemState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Handles text font color changes in board item list.
     */
    private fun changeTextFontColor(newColor: Color) {
        val isCurrentlySelected: (BoardItemModel) -> Boolean = { selectedItem.value?.id == it.id }

        _items.beginOperation()
            .find(isCurrentlySelected)
            .replaceItem { item, _ ->
                (item as? BoardTextItemModel)?.toCopy(
                    ValueModel.TextStyle(
                        textStyle = item.textStyle.copy(color = newColor)
                    )
                )?.updateModifyInfo()
            }
            .saveNewItemState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Updates the [TextStyle] of the item for styling like Bold, Italic, Underline, Strikethrough.
     *
     * Updates modify information, saves a new backstack entry and updates remote database.
     */
    fun changeTextStyle(id: String, newTextStyle: TextStyle) {
        val isThisItem: (BoardItemModel) -> Boolean = { id == it.id }

        _items.beginOperation()
            .find(isThisItem)
            .replaceItemWithCondition(
                condition = { item, _ ->
                    item is BoardTextItemModel
                }) { item, _ ->
                (item as BoardTextItemModel).updateModifyInfo().toCopy(
                    ValueModel.TextStyle(
                        textStyle = item.textStyle.copy(
                            fontStyle = newTextStyle.fontStyle,
                            fontWeight = newTextStyle.fontWeight,
                            textDecoration = newTextStyle.textDecoration
                        )
                    )
                )
            }
            .saveNewItemState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Adds new [boardItem] to the board. Saves [boardItem] to database and new board content to
     * backstack.
     */
    private fun addBoardItem(boardItem: BoardItemModel) {
        _items.beginOperation()
            .add(boardItem)
            .saveNewItemState()
            .updateSelectedItem()
            .finishOperation()
    }

    /**
     * Restores board content to the previous state from backstack.
     */
    fun undoAction() {
        if (isEnableUndoActionState == true) {
            changeCurrentBoardList(index = --boardStackPosition)
        }
    }

    /**
     * Restores board content to the next state in backstack.
     */
    fun redoAction() {
        if (isEnableRedoActionState == true) {
            changeCurrentBoardList(index = ++boardStackPosition)
        }
    }

    /**
     * Disables EditMode on a given item. If item was in EditMode and its text changed, updates its
     * modify information. Removes block from this item.
     */
    private fun BoardItemOperation<BoardItemModel>.disableEditMode(): BoardItemOperation<BoardItemModel> {
        return replaceItemWithCondition(
            condition = { item, _ -> item != null && item.isEditMode }
        ) { item, _ ->
            val newItem = if (isTextActuallyChanged(item)) {
                item.updateModifyInfo()
            } else item

            newItem.unblockItem().toCopy(ValueModel.Edit(isEditMode = false))
        }
    }

    /**
     * Saves item after it was edited.
     *
     * If text was changed, saves to backstack and remote. If it wasn't changed, saves only to remote.
     *
     * Use after disabling EditMode to remotely unblock item and save new text.
     */
    private fun BoardItemOperation<BoardItemModel>.saveEditedItemState(): BoardItemOperation<BoardItemModel> {
        return then { item, items ->
            item?.let {
                if (!item.isEditMode && isTextActuallyChanged(item)) {
                    saveNewItemState(item, items)
                } else {
                    saveItemChangeToRepository(item, items.indexOf(item))
                }
            }
        }
    }

    /**
     * Disables selection on a given item.
     */
    private fun BoardItemOperation<BoardItemModel>.disableSelection(): BoardItemOperation<BoardItemModel> {
        return replaceItemWithCondition(
            condition = { item, _ -> item != null && item.isSelected }
        ) { previouslySelected, _ ->
            previouslySelected.toCopy(ValueModel.Selected(isSelected = false))
        }
    }

    /**
     * Updates current [selectedItem] with value from current [BoardItemOperation].
     */
    private fun BoardItemOperation<BoardItemModel>.updateSelectedItem(): BoardItemOperation<BoardItemModel> {
        return then { item, _ ->
            selectedItem.tryEmit(item)
        }
    }

    /**
     * Updates current [selectedItem] with a given [value].
     */
    private fun BoardItemOperation<BoardItemModel>.updateSelectedItem(
        value: BoardItemModel?,
    ): BoardItemOperation<BoardItemModel> {
        return then { _, _ ->
            selectedItem.tryEmit(value)
        }
    }

    /**
     * Saves the value of current [BoardItemOperation] to backstack and database, if it's not
     * null.
     */
    private fun BoardItemOperation<BoardItemModel>.saveNewItemState(): BoardItemOperation<BoardItemModel> {
        return then { item, items ->
            if (item != null) {
                saveNewItemState(item, items)
            }
        }
    }

    /**
     * Saves list of items of current [BoardItemOperation] to backstack and database.
     */
    private fun BoardItemOperation<BoardItemModel>.saveItemsState(): BoardItemOperation<BoardItemModel> {
        return then { _, items ->
            saveBoardChangeState(items)
        }
    }

    /**
     * Updates the last font size of different types of board items to create new item with
     * corresponding size.
     */
    private fun BoardItemOperation<BoardItemModel>.updateLastFontSize(): BoardItemOperation<BoardItemModel> {
        return then { item, _ ->
            (item as? BoardTextItemModel)?.let { curItem ->
                when (item) {
                    is StickyModel -> {
                        lastStickyFontSize = curItem.textStyle.fontSize
                    }

                    is ShapeModel -> {
                        lastShapeFontSize = curItem.textStyle.fontSize
                    }

                    is TextModel -> {
                        lastTextFontSize = curItem.textStyle.fontSize
                    }
                }
            }
        }
    }

    private fun SnapshotStateList<Color>.updateColor(color: Color) {
        val isRemovedColor: Boolean = this.remove(color)
        if (isRemovedColor) {
            this.add(0, color)
        }
    }

    //////////////////////////////////////////////////
    //            Remote state operations           //
    //////////////////////////////////////////////////

    /**
     * Updates current internal board state with external data (e.g. from some repository).
     *
     * Clears back stack, if this board was modified by other user (or other device).
     * That prevents users from undoing/redoing changes made by other users.
     */
    suspend fun updateBoardFromRemote(remoteBoard: BoardEntity) {
        if (remoteBoard.isModifiedByOtherUser()) {
            clearBackStack()
        }

        updateBoardStateFromRemote(remoteBoard.content)
    }

    /*
     * As other user modifies an item, it's locked for other users (isSelected and isEditMode are
     * disabled). By updating local items' state only when the remote state is more recent should
     * keep their local isSelected and isEditMode states for this user.
     *
     * This is used to initialize board from remote. Currently there is no support for offline mode.
     */
    /**
     * Updates current internal board content with remote content (e.g. from some repository).
     *
     * If remote changes affected currently selected item, update it.
     *
     * If back stack is empty or was cleared, it's initializes with current board state.
     *
     * New local board content will be:
     * 1. If remote content is null, then either remote board was empty, or all items were deleted.
     *    New content should be empty.
     * 2. If local content is empty, but remote is not. Take remote content.
     * 3. If local and remote contents are not empty, merge more recently updated items to local.
     */
    private suspend fun updateBoardStateFromRemote(remoteContent: List<BoardItemModel>?) {
        val newContent: List<BoardItemModel> = when {
            remoteContent == null -> emptyList()
            _items.isEmpty() -> addBlockingInformation(remoteContent)
            else -> mergeLocalBoardStateWithRemote(remoteContent)
        }

        // Clear selected item, if it was changed or blocked by other user.
        selectedItem.value?.let { currentlySelected ->
            val updated = newContent.find { it.id == currentlySelected.id }
            if (updated?.isSelected != true) {
                selectedItem.tryEmit(null)
            }
        }

        _items.swapList(newContent)

        if (boardStack.isEmpty()) {
            boardStack.add(newContent)
        }
    }

    /**
     * Merges local and remote state. If local item state is more current, keeps it. Otherwise a
     * remote item state replaces local.
     */
    private suspend fun mergeLocalBoardStateWithRemote(remoteState: List<BoardItemModel>): List<BoardItemModel> {
        return remoteState.map { remoteItem ->
            val localItem = _items.find { it.id == remoteItem.id }

            if (localItem != null && isLocalItemRelevant(localItem, remoteItem)) {
                localItem.processBlockingToken()
            } else {
                remoteItem.processBlockingToken()
            }
        }
    }

    /**
     * For each [BoardItemModel] in [items], processed its [BlockingToken] and adds proper blocking
     * [UserInfo].
     */
    private suspend fun addBlockingInformation(items: List<BoardItemModel>): List<BoardItemModel> {
        return items.map { remoteItem ->
            remoteItem.processBlockingToken()
        }
    }

    //////////////////////////////////////////////////
    //            Persistent operations             //
    //////////////////////////////////////////////////

    /**
     * Persistently saves [item] in repository and saves the board state in back stack.
     *
     * Will be called after updating local board state to properly save current board state in
     * back stack.
     * @param item - an updated state of an item to be saved in database.
     * @param items - an updated board content to be saved in backstack.
     */
    private fun saveNewItemState(item: BoardItemModel, items: List<BoardItemModel>) {
        saveItemChangeToRepository(item, items.indexOf(item))
        saveChangeToStack(items)
    }

    /**
     * Persistently removes item by its [itemId] from database and saves new board state to backstack.
     *
     * @param itemId - an id of a removed (deleted) board item.
     * @param items - an updated board content to be saved in backstack.
     */
    private fun saveItemDeletion(itemId: String, items: List<BoardItemModel>) {
        deleteItemFromRepository(itemId)
        saveChangeToStack(items)
    }

    /**
     * Persistently saves board content [items] in repository and saves the board state in back stack.
     *
     * @param items - an updated board content to be saved in database and backstack.
     */
    private fun saveBoardChangeState(items: List<BoardItemModel>) {
        saveBoardChangeToRepository(items)
        saveChangeToStack(items)
    }

    /**
     * Persistently saves a single board [item] in database. Updates current board modification info.
     */
    private fun saveItemChangeToRepository(item: BoardItemModel, position: Int) {
        coroutineScope.launch {
            saveBoardItemUseCase.save(
                boardId = boardInfo.boardId,
                item = item,
                itemPosition = position,
                modifiedBy = boardInfo.userUID,
                modifiedAt = item.modifiedAt.toEpochMilli(),
            )
        }
    }

    /**
     * Persistently saves a single board [item] in database without updating board modification info.
     */
    private fun saveItemChangeToRepositoryNoModified(item: BoardItemModel, position: Int) {
        coroutineScope.launch {
            saveBoardItemUseCase.save(
                boardId = boardInfo.boardId,
                item = item,
                itemPosition = position,
            )
        }
    }

    /**
     * Persistently saves entire board [content] in database.
     *
     * Includes modification information but only UPDATES provided fields in database. It doesn't
     * rewrite whole board state. Thus it cannot be used to add new items or remove existing.
     */
    private fun saveBoardChangeToRepository(content: List<BoardItemModel>) {
        coroutineScope.launch {
            saveBoardStateUseCase(
                boardId = boardInfo.boardId,
                boardContent = content,
                modifiedBy = boardInfo.userUID,
                modifiedAt = Timestamp.now().toEpochMilli(),
            )
        }
    }

    /**
     * Deletes board item by its [itemId] from database. Updates current board modification info.
     */
    private fun deleteItemFromRepository(itemId: String) {
        coroutineScope.launch {
            deleteBoardItemUseCase(
                boardId = boardInfo.boardId,
                itemId = itemId,
                modifiedBy = boardInfo.userUID,
                modifiedAt = Timestamp.now().toEpochMilli(),
            )
        }
    }

    //////////////////////////////////////////////////
    //            BackStack operations              //
    //////////////////////////////////////////////////

    /**
     * Saving current board state to a local back stack for undo/redo operations.
     *
     * If the user navigates back using undo operations and makes changes while not in the end of
     * the current back stack, all the subsequent back stack entries are discarded.
     * The user will then continue his back stack history from his current back stack position.
     *
     * To operate correctly boardStack has to be initialize with an initial board state (for both
     * online and offline modes).
     */
    private fun saveChangeToStack(items: List<BoardItemModel>) {
        while (boardStackPosition != boardStack.lastIndex) {
            boardStack.removeLast()
        }
        boardStack.add(items)
        boardStackPosition++

        updateStackActionState()
    }

    private fun updateStackActionState() {
        _isEnableUndoActionState = boardStackPosition > 0
        _isEnableRedoActionState = boardStackPosition < boardStack.lastIndex
    }

    /**
     * Called with undo/redo operations to restore state from backstack.
     *
     * As a business rule the board and the item affected by undo/redo operations will update its
     * modification information.
     */
    private fun changeCurrentBoardList(index: Int) {
        val backStackItems = restoreBoardStateFromBackstack(index)
        _items.swapList(backStackItems)

        updateStackActionState()

        val currentlySelectedItem: BoardItemModel? = _items.find { it.isSelected }
        selectedItem.tryEmit(currentlySelectedItem)

        saveBoardChangeToRepository(_items)
    }

    /**
     * Provides correct board state based on backstack board state, but also:
     * 1. Blocks the item that was blocked in backstack (ex. after adding sticky or text);
     * 2. Unblocks the item, that wasn't blocked in backstack;
     * 3. Keeps recent block information for all board items to not expire current blocks.
     */
    private fun restoreBoardStateFromBackstack(backstackIndex: Int): List<BoardItemModel> {
        val itemById = _items.associateBy { it.id }
        return boardStack[backstackIndex].map { backStackItem ->
            val localStateItem = itemById[backStackItem.id]

            if (isItemChangeByMeInBackstack(backStackItem, localStateItem)) {
                when {
                    isItemBlockedInBackstack(backStackItem) -> {
                        backStackItem.blockItem().updateModifyInfo()
                    }

                    isItemUnblockedInBackstack(backStackItem, localStateItem) -> {
                        backStackItem.unblockItem().updateModifyInfo()
                    }

                    else -> {
                        backStackItem.copyBlockToken(localStateItem).updateModifyInfo()
                    }
                }
            } else {
                backStackItem.copyBlockToken(localStateItem)
            }
        }
    }

    /**
     * Clears current board backstack.
     */
    private fun clearBackStack() {
        boardStack.clear()
        boardStackPosition = 0
        updateStackActionState()
    }

    //////////////////////////////////////////////////
    //                 Utilities                    //
    //////////////////////////////////////////////////

    /**
     * Checks if [currentItem] text has changed compared to its previous state in back stack.
     */
    private fun isTextActuallyChanged(currentItem: BoardItemModel): Boolean {
        val lastStateItem: BoardItemModel? =
            boardStack.last().firstOrNull { currentItem.id == it.id }

        val lastText = (lastStateItem as? BoardTextItemModel)?.text
        val currentText = (currentItem as? BoardTextItemModel)?.text

        return lastText != currentText
    }

    /**
     * Returns a copy of this [BoardItemModel] with updated modifiedAt and modifiedBy values.
     */
    private fun BoardItemModel.updateModifyInfo() = this.toCopy(
        ValueModel.ModifiedAt(timestamp = Timestamp.now()),
        ValueModel.ModifiedBy(modifiedBy = boardInfo.userUID.userId),
    )

    /**
     * Adds blocking token to an item and launches coroutine that maintains temporary block.
     */
    private fun BoardItemModel.blockItem(): BoardItemModel = this.toCopy(
        ValueModel.BlockToken(
            BlockingToken(
                userUID = boardInfo.userUID,
                timestamp = Timestamp.now().toEpochMilli()
            )
        )
    ).also {
        maintainBlock(id)
    }

    /**
     * Removes blocking token and cancel maintaining temporary block.
     */
    private fun BoardItemModel.unblockItem() = this.toCopy(
        ValueModel.BlockToken(blockingToken = null)
    ).also {
        cancelMaintainBlock()
    }

    /**
     * Determines whether a local board item state is relevant compared to a remote board item.
     *
     * Local item state is considered relevant when:
     * 1. [localItem]'s and [remoteItem]`s blocking tokens are equal AND [remoteItem] wasn't modified
     * after [localItem].
     * 2. Both blocking token exist, but not equal AND local blocking timestamp is more recent.
     * 3. Local blocking token exist and is a relevant block by this device.
     *
     * @param localItem The local board item to compare.
     * @param remoteItem The remote board item to compare.
     * @return `true` if the local item is relevant, `false` otherwise.
     */
    private fun isLocalItemRelevant(
        localItem: BoardItemModel,
        remoteItem: BoardItemModel,
    ): Boolean {
        val localBlockTimestamp = localItem.blockingToken?.timestamp
        val remoteBlockTimestamp = remoteItem.blockingToken?.timestamp

        return when {
            localItem.blockingToken == remoteItem.blockingToken
            -> !remoteItem.isModifiedAfter(localItem)

            localBlockTimestamp != null && remoteBlockTimestamp != null
            -> localBlockTimestamp >= remoteBlockTimestamp

            localBlockTimestamp != null
            -> localItem.isBlockedByMe()

            else -> false
        }
    }

    /**
     * Returns a copy of this item with updated [UserInfo].
     *
     * If this item is blocked by other device, provides appropriate [UserInfo].
     * If this item is not blocked by other device, replaces [UserInfo] with `null`.
     */
    private suspend fun BoardItemModel.processBlockingToken(): BoardItemModel {
        val userInfo: UserInfo? = if (this.isBlockedByOther()) {
            getUserInfoUseCase(blockingToken!!.userUID.userId) // Null check in isBlockedByOther().
        } else {
            null
        }
        return this.toCopy(ValueModel.BlockedBy(isBlockedBy = userInfo))
    }

    /**
     * Checks if this item is blocked by other device.
     */
    private fun BoardItemModel.isBlockedByOther(): Boolean {
        return blockingToken != null
                && blockingToken!!.userUID.deviceId != boardInfo.userUID.deviceId
                && !blockingToken!!.isTokenExpired()
    }

    /**
     * Checks if this item is blocked by current device.
     */
    private fun BoardItemModel.isBlockedByMe(): Boolean {
        return blockingToken != null
                && blockingToken!!.userUID.deviceId == boardInfo.userUID.deviceId
                && !blockingToken!!.isTokenExpired()
    }

    /**
     * Generates UUID for board items.
     */
    private fun generateNewID(): String = UUID.randomUUID().toString()


    /**
     * Maintains block of the provided item by periodically updating blocking token locally and
     * remotely.
     *
     * Cancels current maintain block if was running.
     *
     * @param itemId - id of the item to maintain block on.
     */
    private fun maintainBlock(itemId: String) {
        if (maintainBlockTokenJob != null) {
            maintainBlockTokenJob?.cancel()
        }

        maintainBlockTokenJob = viewModelScope?.launch {
            while (isActive) {
                delay(maintainBlockTimeInterval)

                _items.beginOperation()
                    .find { itemId == it.id }
                    .replaceItemWithCondition(
                        condition = { item, _ -> item?.blockingToken != null }
                    ) { item, _ ->
                        item.toCopy(
                            ValueModel.BlockToken(
                                blockingToken = item.blockingToken!!.copy(
                                    timestamp = Timestamp.now().toEpochMilli()
                                )
                            )
                        )
                    }
                    .then { item, items ->
                        // Stop maintaining if item was deleted or unblocked.
                        item?.blockingToken?.let {
                            // Don't update board modification info to not clear backstack of
                            // other users.
                            saveItemChangeToRepositoryNoModified(item, items.indexOf(item))
                        } ?: cancel()
                    }
                    .finishOperation()
            }
        }
    }

    /**
     * Cancel current maintain block job.
     */
    private fun cancelMaintainBlock() {
        maintainBlockTokenJob?.cancel()
        maintainBlockTokenJob = null
    }

    /**
     * Periodically runs checks for expired blocking tokens. Unblocks local items, if their blocking
     * token has expired.
     */
    private fun launchBlockTokenExpirationChecks() {
        viewModelScope?.launch {
            while (isActive) {
                delay(blockExpirationCheckTimeInterval)

                _items.beginOperation()
                    .find { it.blockingToken != null }
                    .replaceItemWithCondition(
                        condition = { item, _ ->
                            item?.blockingToken != null && item.blockingToken!!.isTokenExpired()
                        }
                    ) { item, _ ->
                        item.toCopy(
                            ValueModel.BlockToken(blockingToken = null),
                            ValueModel.BlockedBy(isBlockedBy = null)
                        )
                    }
                    .finishOperation()
            }
        }
    }

    /**
     * Temporary way to check if a [BoardItemModel] was modified by this user between current and
     * backstack state.
     *
     * Checks if two item states are equal ignoring block, because block token is most likely updated
     * in local state compared to backstack.
     */
    private fun isItemChangeByMeInBackstack(
        backStackItem: BoardItemModel,
        localStateItem: BoardItemModel?,
    ): Boolean {
        return !backStackItem.equalsIgnoringBlock(localStateItem)
    }

    /**
     * Check if this item is blocked in backstack. It is blocked, if it's in EditMode.
     */
    private fun isItemBlockedInBackstack(backStackItem: BoardItemModel): Boolean {
        return backStackItem.isEditMode
    }

    /**
     * Check if this item is blocked in backstack. It is blocked, if it's in EditMode.
     */
    private fun isItemUnblockedInBackstack(
        backStackItem: BoardItemModel,
        localStateItem: BoardItemModel?,
    ): Boolean {
        return localStateItem?.blockingToken != null && backStackItem.blockingToken == null
    }

    /**
     * Returns the copy of this [BoardItemModel] with the block token from [other] board item.
     */
    private fun BoardItemModel.copyBlockToken(other: BoardItemModel?): BoardItemModel {
        return toCopy(ValueModel.BlockToken(other?.blockingToken))
    }

    /**
     * Checks if two [BoardItemModel] are equal ignoring their block token property.
     */
    private fun BoardItemModel.equalsIgnoringBlock(other: BoardItemModel?): Boolean {
        val thisWithoutBlock = this.toCopy(ValueModel.BlockToken(null))
        val otherWithoutBlock = other?.toCopy(ValueModel.BlockToken(null))

        return thisWithoutBlock == otherWithoutBlock
    }

    private fun BoardEntity.isModifiedByOtherUser(): Boolean =
        modifiedBy.deviceId != boardInfo.userUID.deviceId

    private fun BlockingToken.isTokenExpired(): Boolean {
        return checkBlockTokenValidityUseCase.isTokenExpired(this)
    }
}