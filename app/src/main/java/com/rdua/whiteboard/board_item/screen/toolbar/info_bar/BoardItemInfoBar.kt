package com.rdua.whiteboard.board_item.screen.toolbar.info_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.utils.conditional
import com.rdua.whiteboard.board.utils.consumeAllPointerEvents
import com.rdua.whiteboard.board_item.event.ThreeDotsEvent
import com.rdua.whiteboard.board_item.viewmodel.BoardItemViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun BoardItemInfoBar(
    viewModel: BoardItemViewModel,
    consumeClicks: Boolean = true,
) {
    val itemInfo = viewModel.state.selectedItemInfo

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spaces.space5,
                vertical = MaterialTheme.spaces.space2
            )
            .conditional(condition = consumeClicks, ifTrue = { consumeAllPointerEvents() })
            .background(
                color = MaterialTheme.colors.background,
                shape = RoundedCornerShape(MaterialTheme.spaces.space2),
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spaces.space5,
                    vertical = MaterialTheme.spaces.space3,
                ),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colors.textFaint)) {
                        append(stringResource(R.string.created_by))
                    }
                    append(" ")
                    append(itemInfo.createdBy ?: stringResource(R.string.unidentified))
                },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = itemInfo.createdTime ?: stringResource(R.string.unidentified),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spaces.space2))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colors.textFaint)) {
                        append(stringResource(R.string.modified_by))
                    }
                    append(" ")
                    append(itemInfo.modifiedBy ?: stringResource(R.string.unidentified))
                },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = itemInfo.modifiedTime ?: stringResource(R.string.unidentified),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
            )
        }

        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .padding(end = MaterialTheme.spaces.space1, top = MaterialTheme.spaces.space1)
                .clip(CircleShape)
                .clickable {
                    viewModel.onEvent(ThreeDotsEvent.HideInfo)
                },
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.close_button_description),
        )
    }
}