package com.rdua.whiteboard.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_options_dialog.composable.BoardOptionsDialog
import com.rdua.whiteboard.common.composable.HomeTopBar
import com.rdua.whiteboard.common.composable.ProgressIndicator
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.home.event.AllBoardsEvent
import com.rdua.whiteboard.home.state.AllBoardsUIState
import com.rdua.whiteboard.home.viewmodel.AllBoardsViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun AllBoardsScreen(
    viewModel: AllBoardsViewModel = hiltViewModel()
) {
    AllBoardsScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun AllBoardsScreen(
    state: AllBoardsUIState,
    onEvent: (AllBoardsEvent) -> (Unit) = { },
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.boardsBackground)
    ) {
        if (state.boardsList.isNullOrEmpty()) {
            NoBoards(boardsList = state.boardsList, onEvent = onEvent)
        } else {
            BoardsList(state = state, onEvent = onEvent)
        }
    }
}

@Composable
private fun BoardsList(
    state: AllBoardsUIState,
    onEvent: (AllBoardsEvent) -> Unit = { },
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopBar(
            titleTopBar = stringResource(R.string.home_screen_top_bar_text),
            navigationActionsContent = {
                IconButton(onClick = {
                    //Event for filter/sorting
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sorting),
                        contentDescription = stringResource(R.string.more_settings),
                    )
                }
            },
            searchText = "",
            onClickSearchMicrophone = {},
            onSearchValueChange = {}
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(24.dp),
            columns = GridCells.Adaptive(156.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.boardsList!!) { item ->
                BoardItemUI(boardState = item, onEvent = onEvent)
            }
        }
    }

    CreateBoardFloatingButton(onEvent = onEvent)

    if (state.boardOptionsBoardItem != null) {
        BoardOptionsDialog(
            board = state.boardOptionsBoardItem,
            isOpenedDialog = state.isOpenBoardOptions,
            onDismissDialog = { onEvent(AllBoardsEvent.CloseBoardOptions) },
        )
    }
}

@Composable
private fun BoardItemUI(
    boardState: BoardEntity,
    onEvent: (AllBoardsEvent) -> (Unit) = { },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(AllBoardsEvent.NavigateToBoardItem(boardState.id))
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clipboard),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                )
            }
            Divider(
                color = Color.Black,
                modifier = Modifier.alpha(0.2f)
            )
            Row(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = boardState.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        onEvent(AllBoardsEvent.OpenBoardOptions(boardState))
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun NoBoards(boardsList: List<BoardEntity>?, onEvent: (AllBoardsEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.boardsBackground)
    ) {
        HomeTopBar(titleTopBar = stringResource(R.string.home_screen_top_bar_text))

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (boardsList == null) {
                ProgressIndicator()
            } else {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.emptyBoard),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clipboard_no_boards),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(63.dp, 80.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.no_boards_screen_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = MaterialTheme.spaces.space5,
                        end = 0.dp,
                        bottom = 0.dp
                    )
                )
                Text(
                    text = stringResource(R.string.no_boards_screen_message),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = MaterialTheme.spaces.space2,
                        end = 0.dp,
                        bottom = 0.dp
                    )
                )
            }
        }
        CreateBoardFloatingButton(onEvent)
    }
}

@Composable
private fun CreateBoardFloatingButton(onEvent: (AllBoardsEvent) -> Unit) {
    val newBoardTitle = stringResource(id = R.string.new_board_title)
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                onEvent(AllBoardsEvent.NavigateToNewBoard(newBoardTitle))
            },
            modifier = Modifier
                .padding(MaterialTheme.spaces.space4)
                .align(Alignment.BottomEnd),
            shape = CircleShape,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
