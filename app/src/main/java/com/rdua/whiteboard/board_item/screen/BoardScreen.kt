package com.rdua.whiteboard.board_item.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.composable.BoardItemHandlerUI
import com.rdua.whiteboard.board.model.BoardItemInnerModel
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.BoardTextItemModel
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.board.model.LineModel
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.ShapeType
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.board_item.event.BoardEvent
import com.rdua.whiteboard.board_item.event.BoardItemEvent
import com.rdua.whiteboard.board_item.event.BoardToolBarEvent
import com.rdua.whiteboard.board_item.event.TextToolbarEvent
import com.rdua.whiteboard.board_item.screen.toolbar.NoteToolBar
import com.rdua.whiteboard.board_item.screen.toolbar.color_bar.ColorOption
import com.rdua.whiteboard.board_item.screen.toolbar.color_bar.ColorRangeBar
import com.rdua.whiteboard.board_item.screen.toolbar.info_bar.BoardItemInfoBar
import com.rdua.whiteboard.board_item.screen.toolbar.more_toolbar.ThreeDotsToolbar
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.FontSizeRangeBar
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.TextToolbar
import com.rdua.whiteboard.board_item.utils.clickableWithNoIndication
import com.rdua.whiteboard.board_item.viewmodel.BoardItemViewModel
import com.rdua.whiteboard.board_options_dialog.composable.BoardOptionsDialog
import com.rdua.whiteboard.common.composable.HomeDivider
import com.rdua.whiteboard.common.composable.HomeTopBar
import com.rdua.whiteboard.common.composable.ProgressIndicator
import com.rdua.whiteboard.common.composable.RectangleBottomSheet
import com.rdua.whiteboard.common.composable.WhiteboardSnackbarHost
import com.rdua.whiteboard.common.manager.snackbar.rememberSnackbarFlowState
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.data.entities.boards.BoardEntityState
import com.rdua.whiteboard.ui.theme.MaterialTheme


@Composable
fun BoardScreen(
    viewModel: BoardItemViewModel = hiltViewModel(),
) {
    val boardEntity: BoardEntityState? by viewModel.boardEntityState.collectAsState()

    boardEntity?.let { board ->
        when(board) {
            is BoardEntityState.Loading -> ProgressIndicator()
            is BoardEntity -> BoardItemScreenUI(viewModel = viewModel, board = board)
        }
    }
}

@Composable
private fun BoardItemScreenUI(
    viewModel: BoardItemViewModel,
    board: BoardEntity,
) {
    val selectedItem: BoardItemModel? by viewModel.selectedItem.collectAsState()
    val snackbarHostState = rememberSnackbarFlowState(
        snackbarFlow = viewModel.snackbarFlow
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colors.boardsBackground,
        topBar = {
            EventTopBar(
                viewModel = viewModel,
                title = board.title,
            )
        },
        bottomBar = {
            BoardScreenBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .clickableWithNoIndication {
                        viewModel.onEvent(BoardItemEvent.DismissSelectBoardItem)
                    },
                selectedItem = selectedItem,
                viewModel = viewModel,
            )
        },
        snackbarHost = {
            WhiteboardSnackbarHost(hostState = snackbarHostState)
        },
        content = {
            BoardContentUI(
                paddingValues = it,
                viewModel = viewModel,
            )

            BoardOptionsDialog(
                board = board,
                isOpenedDialog = viewModel.state.isOpenBoardOptions,
                onDismissDialog = { viewModel.onEvent(BoardEvent.CloseBoardOptions) },
            )
        }
    )
}

@Composable
private fun BoardContentUI(
    paddingValues: PaddingValues,
    viewModel: BoardItemViewModel,
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .clickableWithNoIndication {
                viewModel.onEvent(BoardItemEvent.DismissSelectBoardItem)
            }
            //TODO Until Board Feature is implemented Board size is limited by the screen size minus
            // scaffold's top/bottom bars. Large board items may not fit inside board content resulting
            // in layout drifting. This is a temp fix to allow Board content container to resize to
            // fit board items of any size.
            .wrapContentSize(unbounded = true, align = Alignment.TopStart),
    ) {

        BoardItemsUI(viewModel = viewModel)
        UndoRedoActionBar(viewModel)
        ShapesBottomSheet(viewModel)
    }
}

@Composable
private fun BoardScreenBottomBar(
    selectedItem: BoardItemModel?,
    viewModel: BoardItemViewModel,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = selectedItem?.isSelected == true,
        label = "", // Optional parameter, but AS shows warnings if omitted.
        transitionSpec = {
            slideInVertically { it } togetherWith slideOutVertically { it }
        },
    ) { isSelected ->
        if (!isSelected) {
            BottomEventButtons(viewModel::onEvent)
        } else {
            SelectedBoardItemToolBar(
                viewModel = viewModel,
                selectedItem = selectedItem,
            )
        }
    }
}

@Composable
private fun BoardItemsUI(
    viewModel: BoardItemViewModel,
) {
    viewModel.boardItems
        .sortedBy { it is BoardItemInnerModel }
        // Sorts the list of board items to first draw all FrameModels, and then all other items.
        // The original order of boardItems is not changed.
        .forEach { item ->
            BoardItemUI(
                model = item,
                onBoardItemEvent = viewModel::onEvent,
            )
        }
}

/**
 * Main toolbar for selected item.
 *
 * Currently its parent composable has a [Modifier.clickable] that deselects the selected item. By
 * setting [consumeClicks] to `true` the toolbar itself and its popups will consume any pointer input,
 * while allowing the empty space around them to remain interactable.
 */
@Composable
private fun SelectedBoardItemToolBar(
    viewModel: BoardItemViewModel,
    selectedItem: BoardItemModel?,
    consumeClicks: Boolean = true,
) {
    val state = viewModel.state
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            selectedItem?.let { model ->
                BackHandler(true) {
                    viewModel.onEvent(BoardItemEvent.DismissSelectBoardItem)
                }
                ColorToolBar(viewModel = viewModel, model = model)
                if (state.isOpenFontSizeToolbar && model is BoardTextItemModel) {
                    FontSizeRangeBar(viewModel = viewModel, model = model)
                }
                if (state.isOpenThreeDotsToolbar) {
                    ThreeDotsToolbar(viewModel = viewModel, model = model)
                }
                if (state.isOpenTextToolbar && model is BoardTextItemModel) {
                    TextToolbar(viewModel = viewModel, model = model, consumeClicks = consumeClicks)
                }
                if (state.isOpenBoardItemInfoBar) {
                    BoardItemInfoBar(viewModel = viewModel, consumeClicks = consumeClicks)
                }

                if (model.isSelected) {
                    NoteToolBar(model = model, viewModel = viewModel, consumeClicks = consumeClicks)
                }
            }
        }
    }
}

@Composable
private fun ColorToolBar(
    viewModel: BoardItemViewModel,
    model: BoardItemModel,
) {
    if (viewModel.state.isOpenFontColorBar) {
        val fontColors: List<Color>? =
            when (model) {
                is ShapeModel -> viewModel.shapeFontColors
                is StickyModel -> viewModel.stickyFontColors
                is TextModel -> viewModel.textFontColors
                is FrameModel, is LineModel -> null
            }

        if (fontColors != null) {
            ColorRangeBar(
                colorOption = ColorOption.FontColor,
                colors = fontColors,
                onSelectColor = { color ->
                    viewModel.onEvent(TextToolbarEvent.SelectedFontColor(color = color))
                },
            )
        }
    }

    if (viewModel.state.isOpenBorderColorBar) {
        val borderColors: List<Color>? =
            when (model) {
                is ShapeModel -> viewModel.shapeBorderColors
                is FrameModel -> viewModel.frameBorderColors
                is TextModel, is StickyModel, is LineModel -> null
            }

        if (borderColors != null) {
            ColorRangeBar(
                colorOption = ColorOption.BorderColor,
                colors = borderColors,
                onSelectColor = { color ->
                    viewModel.onEvent(BoardToolBarEvent.SelectedBorderColor(color = color))
                },
            )
        }
    }

    if (viewModel.state.isOpenBackgroundColorBar) {
        val backgroundColors: List<Color>? =
            when (model) {
                is StickyModel -> viewModel.stickyColors
                is ShapeModel -> viewModel.shapeColors
                is FrameModel -> viewModel.frameColors
                is LineModel -> viewModel.lineColors
                is TextModel -> null
            }

        if (backgroundColors != null) {
            ColorRangeBar(
                colorOption = ColorOption.BackgroundColor,
                colors = backgroundColors,
                onSelectColor = { color ->
                    viewModel.onEvent(BoardToolBarEvent.SelectedBackgroundColor(color = color))
                },
            )
        }
    }
}

@Composable
private fun BoardItemUI(
    model: BoardItemModel,
    onBoardItemEvent: (BoardItemEvent) -> Unit,
) {
    BoardItemHandlerUI(
        model = model,
        onSelect = { id ->
            onBoardItemEvent(BoardItemEvent.Select(id))
        },
        onEnableEditMode = { id ->
            onBoardItemEvent(BoardItemEvent.EnableEditMode(id))
        },
        onDisableEditMode = { id ->
            onBoardItemEvent(BoardItemEvent.DisableEditMode(id))
        },
        onMove = { id, offset ->
            onBoardItemEvent(BoardItemEvent.PositionChange(id, offset))
        },
        onDragEnd = { id ->
            onBoardItemEvent(BoardItemEvent.EndChangePosition(id))
        },
        onTextChange = { id, newText ->
            onBoardItemEvent(BoardItemEvent.TextChange(id, newText))
        },
        onSizeChanged = { id, size, offset ->
            onBoardItemEvent(BoardItemEvent.SizeChange(id, size, offset))
        },
        onResizeEnd = { id ->
            onBoardItemEvent(BoardItemEvent.EndResize(id))
        },
        onScale = { id, scale, offset ->
            onBoardItemEvent(BoardItemEvent.ScaleChange(id, scale, offset))
        },
        onTextScale = { id, newFont, offset ->
            onBoardItemEvent(BoardItemEvent.TextScaleChange(id, newFont, offset))
        },
        onDisableAutoTextWidth = { id ->
            onBoardItemEvent(BoardItemEvent.DisableAutoTextWidth(id))
        },
        onFontSizeChange = { id, newFont ->
            onBoardItemEvent(BoardItemEvent.FontSizeChange(id, newFont))
        },
        onSetMaxFontSize = { id, maxFont ->
            onBoardItemEvent(BoardItemEvent.SetMaxFontSize(id, maxFont))
        },
        onAutoFontSizeModeChange = { id, isEnabled ->
            onBoardItemEvent(BoardItemEvent.AutoFontSizeModeChange(id, isEnabled))
        },
        onTextSizeChanged = { id, size ->
            onBoardItemEvent(BoardItemEvent.TextSizeChange(id, size))
        },
        onResizeLine = { lineData ->
            onBoardItemEvent(BoardItemEvent.ResizeLine(lineData))
        }
    )
}

@Composable
fun UndoRedoActionBar(
    viewModel: BoardItemViewModel,
) {
    Column(
        modifier = Modifier
            .padding(start = MaterialTheme.spaces.space6, top = MaterialTheme.spaces.space10)
    ) {
        val cornerPercent = 50

        val isEnableUndoAction = viewModel.isEnableUndoAction
        val isEnableRedoAction = viewModel.isEnableRedoAction

        if (isEnableUndoAction != null && isEnableRedoAction != null) {
            ActionItemButton(
                roundedCornerShape = RoundedCornerShape(
                    topStartPercent = cornerPercent, topEndPercent = cornerPercent
                ),
                eventButton = EventButton.UndoButton,
                eventOnClick = viewModel::onEvent,
                isEnabled = isEnableUndoAction
            )

            HomeDivider(
                modifier = Modifier
                    .size(width = MaterialTheme.spaces.space10, height = MaterialTheme.spaces.space)
            )

            ActionItemButton(
                roundedCornerShape = RoundedCornerShape(
                    bottomStartPercent = cornerPercent, bottomEndPercent = cornerPercent
                ),
                eventButton = EventButton.RedoButton,
                eventOnClick = viewModel::onEvent,
                isEnabled = isEnableRedoAction
            )
        }
    }
}

@Composable
private fun ActionItemButton(
    roundedCornerShape: RoundedCornerShape,
    eventButton: EventButton,
    eventOnClick: (BoardItemEvent) -> Unit,
    isEnabled: Boolean,
) {
    TextButton(
        onClick = { eventButton.event?.let { eventOnClick(it) } },
        modifier = Modifier
            .clip(shape = roundedCornerShape)
            .background(color = Color.White)
            .size(MaterialTheme.spaces.space10),
        enabled = isEnabled
    ) {
        Image(
            painterResource(id = eventButton.iconId),
            contentDescription = stringResource(eventButton.titleId),
            alpha = if (isEnabled) ContentAlpha.high else ContentAlpha.disabled
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShapesBottomSheet(viewModel: BoardItemViewModel) {
    RectangleBottomSheet(
        openBottomSheet = viewModel.state.isOpenShapesBottomSheet,
        onDismissRequest = { viewModel.onEvent(BoardItemEvent.CloseBottomSheetShape) },
    ) {
        Column(modifier = Modifier.padding(vertical = MaterialTheme.spaces.space3)) {
            Text(
                text = stringResource(R.string.shapes),
                modifier = Modifier.padding(MaterialTheme.spaces.space3),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            HomeDivider()
            Spacer(modifier = Modifier.height(MaterialTheme.spaces.space2))

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                contentPadding = PaddingValues(MaterialTheme.spaces.space4),
            ) {
                items(ShapeItem.shapeItemList) { shapeItem ->
                    ShapeItemUI(shapeItem = shapeItem, viewModel::addShape)
                }
            }
        }
    }
}

@Composable
fun ShapeItemUI(
    shapeItem: ShapeItem,
    addShape: (ShapeType) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(MaterialTheme.spaces.space12)
            .clickable { addShape(shapeItem.type) },
    ) {
        Icon(
            painter = painterResource(shapeItem.iconId),
            contentDescription = stringResource(shapeItem.titleId),
        )
    }
}

@Composable
private fun EventTopBar(
    viewModel: BoardItemViewModel,
    title: String?,
) {
    HomeTopBar(
        titleTopBar = title ?: "",
        onBackPressed = { viewModel.onEvent(BoardItemEvent.NavigateBackEvent) },
        navigationActionsContent = {
            IconButton(
                onClick = { viewModel.onEvent(BoardEvent.OpenBoardOptions) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more_settings),
                    contentDescription = stringResource(R.string.more_settings),
                )
            }
            IconButton(onClick = { viewModel.onEvent(BoardItemEvent.ShareEvent) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = stringResource(R.string.share),
                )
            }
        },
        searchText = viewModel.state.searchText,
        onClickSearchMicrophone = { viewModel.onEvent(BoardItemEvent.MicrophoneEvent) },
        onSearchValueChange = { viewModel.onEvent(BoardItemEvent.SearchTextChangeEvent(it)) }
    )
}

@Composable
private fun BottomEventButtons(
    onEvent: (BoardItemEvent) -> Unit,
) {
    var isDropDownMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        HomeDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    top = MaterialTheme.spaces.space1,
                    start = MaterialTheme.spaces.space2,
                    end = MaterialTheme.spaces.space2,
                    bottom = MaterialTheme.spaces.space2
                )
        ) {
            EventButton(
                modifier = Modifier.weight(1f),
                eventButton = EventButton.StickyButton,
            ) { event -> event?.let { onEvent(it) } }

            EventButton(
                modifier = Modifier.weight(1f),
                eventButton = EventButton.TextButton,
            ) { event -> event?.let { onEvent(it) } }

            EventButton(
                modifier = Modifier.weight(1f),
                eventButton = EventButton.ShapesButton,
            ) { event -> event?.let { onEvent(it) } }

            EventButton(
                modifier = Modifier.weight(1f),
                eventButton = EventButton.ImageButton,
            ) { event -> event?.let { onEvent(it) } }

            EventButton(
                modifier = Modifier.weight(1f),
                eventButton = EventButton.MoreButton
            ) { isDropDownMenuOpen = true }

            Box(contentAlignment = Alignment.BottomEnd) {
                DropdownMenu(
                    modifier = Modifier.background(Color.White),
                    expanded = isDropDownMenuOpen,
                    onDismissRequest = { isDropDownMenuOpen = false },
                ) {

                    EventDropdownItemUI(
                        eventButton = EventButton.CreateFrameButton,
                        eventOnClick = { event ->
                            event?.let { onEvent(it) }
                            isDropDownMenuOpen = false
                        }
                    )
                    EventDropdownItemUI(
                        eventButton = EventButton.AddCommentButton,
                        eventOnClick = { event ->
                            event?.let { onEvent(it) }
                            isDropDownMenuOpen = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventDropdownItemUI(
    eventButton: EventButton,
    eventOnClick: (BoardItemEvent?) -> Unit,
) {
    DropdownMenuItem(
        onClick = { eventOnClick(eventButton.event) },
        text = {
            Text(
                text = stringResource(id = eventButton.titleId),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = MaterialTheme.spaces.space2)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(eventButton.iconId),
                contentDescription = stringResource(eventButton.titleId),
            )
        }
    )
}

@Composable
private fun EventButton(
    modifier: Modifier,
    eventButton: EventButton,
    eventOnClick: (BoardItemEvent?) -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = { eventOnClick(eventButton.event) },
        contentPadding = PaddingValues(horizontal = MaterialTheme.spaces.space2)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(eventButton.iconId),
                contentDescription = stringResource(eventButton.titleId),
                tint = Color.Unspecified,
            )
            Text(
                text = stringResource(eventButton.titleId),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun BoardScreenPreview() {
    BoardScreen()
}
