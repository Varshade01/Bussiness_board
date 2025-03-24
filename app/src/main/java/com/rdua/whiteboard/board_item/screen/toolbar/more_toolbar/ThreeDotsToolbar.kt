package com.rdua.whiteboard.board_item.screen.toolbar.more_toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.model.BoardItemInnerModel
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board_item.event.ThreeDotsEvent
import com.rdua.whiteboard.board_item.viewmodel.BoardItemViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun ThreeDotsToolbar(viewModel: BoardItemViewModel, model: BoardItemModel) {

    Box {
        DropdownMenu(
            expanded = viewModel.state.isOpenThreeDotsToolbar,
            onDismissRequest = { viewModel.onEvent(ThreeDotsEvent.CloseToolbar) },
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(MaterialTheme.spaces.space1)
                )
                .padding(
                    horizontal = MaterialTheme.spaces.space4,
                    vertical = MaterialTheme.spaces.space3
                )
        ) {
            ThreeDotsTextOptions(
                stringResourceID = R.string.three_dots_copy_link,
                viewModel = viewModel,
                event = ThreeDotsEvent.CopyLink(model.id)
            )
            ThreeDotsTextOptions(
                stringResourceID =
                if (model.isLocked) R.string.three_dots_unlock else R.string.three_dots_lock,
                viewModel = viewModel,
                event =
                if (model.isLocked) ThreeDotsEvent.Unlock(model.id) else ThreeDotsEvent.Lock(model.id)
            )
            ThreeDotsTextOptions(
                enabled = !model.isLocked && model is BoardItemInnerModel && model.outerFrameId == null,
                stringResourceID = R.string.create_frame,
                viewModel = viewModel,
                event = ThreeDotsEvent.CreateFrame(model.id)
            )
            ThreeDotsTextOptions(
                stringResourceID = R.string.three_dots_info,
                viewModel = viewModel,
                event = ThreeDotsEvent.ShowInfo(model.id)
            )
            ThreeDotsTextOptions(
                enabled = !model.isLocked,
                stringResourceID = R.string.three_dots_bring_front,
                viewModel = viewModel,
                event = ThreeDotsEvent.BringFront(model.id)
            )
            ThreeDotsTextOptions(
                enabled = !model.isLocked,
                stringResourceID = R.string.three_dots_send_back,
                viewModel = viewModel,
                event = ThreeDotsEvent.SendBack(model.id)
            )
        }
    }
}

@Composable
fun ThreeDotsTextOptions(
    enabled: Boolean = true,
    stringResourceID: Int,
    viewModel: BoardItemViewModel,
    event: ThreeDotsEvent,
) {
    if (enabled) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.onEvent(event) }) {
            Text(
                text = stringResource(stringResourceID),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(600),
                ),
                modifier = Modifier
                    .padding(MaterialTheme.spaces.space3)
            )
        }
    }
}
