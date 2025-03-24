package com.rdua.whiteboard.board_item.screen.toolbar.text_bar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.model.BoardTextItemModel
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.board.utils.conditional
import com.rdua.whiteboard.board.utils.consumeAllPointerEvents
import com.rdua.whiteboard.board.utils.onKeyboardDismiss
import com.rdua.whiteboard.board_item.event.TextToolbarEvent
import com.rdua.whiteboard.board_item.viewmodel.BoardItemViewModel
import com.rdua.whiteboard.ui.theme.Gray30Alpha50
import com.rdua.whiteboard.ui.theme.MaterialTheme

val verticalAlignment = Alignment.CenterVertically
val buttonTextSize = 20.sp
val buttonInternalPadding = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToolbar(
    viewModel: BoardItemViewModel,
    model: BoardTextItemModel,
    consumeClicks: Boolean = true,
) {
    val navigationBarWindowInsets = NavigationBarDefaults.windowInsets
    // Consume navigationBar and Scaffold's bottomBar height to show this toolbar right
    // on top of ime keyboard.
    val bottomPaddingToConsume = with(LocalDensity.current) {
        remember {
            val navigationBarHeight = navigationBarWindowInsets.getBottom(this).toDp()
            navigationBarHeight + BottomSheetDefaults.SheetPeekHeight
        }
    }

    Column(
        modifier = Modifier
            .padding(bottom = 14.dp)
            .conditional(condition = consumeClicks, ifTrue = { consumeAllPointerEvents() })
            // consumeWindowInsets BEFORE adding imePadding()
            .consumeWindowInsets(WindowInsets(bottom = bottomPaddingToConsume))
            .imePadding()
            .clip(shape = RoundedCornerShape(MaterialTheme.spaces.space5))
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier.padding(
                start = MaterialTheme.spaces.space4,
                top = MaterialTheme.spaces.space4,
                end = MaterialTheme.spaces.space4
            ),
            verticalAlignment = verticalAlignment
        ) {

            TextStylePart(viewModel = viewModel, model = model)

            SetDividerTextSizePart(14.dp, 6.dp)

            TextSizePart(viewModel = viewModel, model = model)

            SetDividerTextSizePart(6.dp, 14.dp)

            TextFontColorButton(viewModel = viewModel, model = model)
        }

        Spacer(modifier = Modifier.padding(MaterialTheme.spaces.space2))

        Row(
            modifier = Modifier.padding(
                start = MaterialTheme.spaces.space4,
                end = MaterialTheme.spaces.space4,
                bottom = MaterialTheme.spaces.space4,
            )
        ) {
            TextHorizontalAlignmentPart(
                modelHorizontalTextAlign = model.textStyle.textAlign,
                onEvent = viewModel::onEvent,
            )

            SetDivider(14.dp)

            if (model !is TextModel) {
                TextVerticalAlignmentPart(
                    onEvent = viewModel::onEvent,
                    modelVerticalTextAlignment = model.verticalTextAlignment
                )
            }
        }
    }
}

@Composable
private fun TextFontColorButton(
    viewModel: BoardItemViewModel,
    model: BoardTextItemModel,
) {
    val textFontColor = model.textStyle.color
    val buttonShape = RoundedCornerShape(size = 6.dp)

    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = textFontColor,
                shape = buttonShape
            )
            .conditional(
                condition = (textFontColor == Color.White),
                ifTrue = {
                    border(
                        width = (0.5).dp,
                        color = Gray30Alpha50,
                        shape = buttonShape,
                    )
                },
            )
            .clickable {
                viewModel.onEvent(TextToolbarEvent.ToggleFontColorBar)
            }
    )
}

@Composable
fun TextStylePart(viewModel: BoardItemViewModel, model: BoardTextItemModel) {
    val styleState = model.textStyle.toTextStyleState()

    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(9.4.dp)
    ) {
        SetBoldText(styleState = styleState, onEvent = viewModel::onEvent)
        SetItalicText(styleState = styleState, onEvent = viewModel::onEvent)
        SetUnderlineText(styleState = styleState, onEvent = viewModel::onEvent)
        SetStrikeThroughText(styleState = styleState, onEvent = viewModel::onEvent)
    }
}

@Composable
fun TextSizePart(viewModel: BoardItemViewModel, model: BoardTextItemModel) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.space),
    ) {
        val focusManager = LocalFocusManager.current
        val currentFontSize = model.textStyle.fontSize.value

        Icon(
            painter = painterResource(id = R.drawable.ic_minus),
            contentDescription = stringResource(R.string.text_toolbar_icon_minus_description),
            tint = if (currentFontSize <= DefaultTextStyles.minFontSize.value) Color.LightGray else Color.Black,
            modifier = Modifier
                .clickable(enabled = currentFontSize > DefaultTextStyles.minFontSize.value) {
                    viewModel.onEvent(TextToolbarEvent.ScaleDownText)
                    viewModel.onEvent(TextToolbarEvent.SetAutoMode(false))
                }
        )

        BasicTextField(
            value = viewModel.state.fontSizeInput,
            onValueChange = {
                viewModel.onEvent(TextToolbarEvent.EnterFontSize(it))
                viewModel.onEvent(TextToolbarEvent.SetAutoMode(false))
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .size(width = 39.dp, height = 20.dp)
                .onFocusChanged {
                    if (it.isFocused) {
                        viewModel.onEvent(TextToolbarEvent.ToggleFontSizeToolbar)
                    }
                }
                .onKeyboardDismiss {
                    focusManager.clearFocus()
                    // event to viewmodel to correct fontSizeInputState.
                    viewModel.onEvent(TextToolbarEvent.UpdateFontSizeText)
                }
        )

        val curMaxSize = model.maxFontSize.value

        Icon(
            painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = stringResource(R.string.text_toolbar_icon_plus_description),
            tint = if (currentFontSize >= curMaxSize) Color.LightGray else Color.Black,
            modifier = Modifier.clickable(enabled = currentFontSize < curMaxSize) {
                viewModel.onEvent(TextToolbarEvent.ScaleUpText)
                viewModel.onEvent(TextToolbarEvent.SetAutoMode(false))
            }
        )
    }
}

@Composable
fun TextHorizontalAlignmentPart(
    modelHorizontalTextAlign: TextAlign?,
    onEvent: (TextToolbarEvent) -> Unit,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_text_align_start),
            contentDescription = stringResource(R.string.text_toolbar_icon_align_start_description),
            modifier = Modifier
                .background(
                    color = getButtonBackgroundColor(
                        isButtonPressed = modelHorizontalTextAlign == TextAlign.Start
                    ),
                )
                .clickable(
                    enabled = modelHorizontalTextAlign != TextAlign.Start,
                    onClick = {
                        onEvent(TextToolbarEvent.HorizontalTextAlign(textAlign = TextAlign.Start))
                    }
                )
        )

        Image(
            painter = painterResource(id = R.drawable.ic_text_align_center),
            contentDescription = stringResource(R.string.text_toolbar_icon_align_center_description),
            modifier = Modifier
                .background(
                    color = getButtonBackgroundColor(
                        isButtonPressed = modelHorizontalTextAlign == TextAlign.Center
                    ),
                )
                .clickable(
                    enabled = modelHorizontalTextAlign != TextAlign.Center,
                    onClick = {
                        onEvent(TextToolbarEvent.HorizontalTextAlign(textAlign = TextAlign.Center))
                    }
                )
        )

        Image(
            painter = painterResource(id = R.drawable.ic_text_align_end),
            contentDescription = stringResource(R.string.text_toolbar_icon_align_end_description),
            modifier = Modifier
                .background(
                    color = getButtonBackgroundColor(
                        isButtonPressed = modelHorizontalTextAlign == TextAlign.End
                    ),
                )
                .clickable(
                    enabled = modelHorizontalTextAlign != TextAlign.End,
                    onClick = {
                        onEvent(TextToolbarEvent.HorizontalTextAlign(textAlign = TextAlign.End))
                    }
                )
        )
    }
}

@Composable
fun TextVerticalAlignmentPart(
    onEvent: (TextToolbarEvent) -> Unit,
    modelVerticalTextAlignment: Alignment?,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.space3)
    ) {
        AlignmentArrowIcon(
            isEnabled = modelVerticalTextAlignment != Alignment.TopCenter,
            backgroundColor = getButtonBackgroundColor(
                isButtonPressed = modelVerticalTextAlignment == Alignment.TopCenter
            ),
            onClick = {
                onEvent(TextToolbarEvent.VerticalTextAlignment(alignment = Alignment.TopCenter))
            },
            resourceId = R.drawable.ic_arrow_alignment_up,
            contentDescriptionId = R.string.text_toolbar_icon_alignment_top_description
        )

        AlignmentArrowIcon(
            isEnabled = modelVerticalTextAlignment != Alignment.BottomCenter,
            backgroundColor = getButtonBackgroundColor(
                isButtonPressed = modelVerticalTextAlignment == Alignment.BottomCenter
            ),
            onClick = {
                onEvent(TextToolbarEvent.VerticalTextAlignment(alignment = Alignment.BottomCenter))
            },
            resourceId = R.drawable.ic_arrow_alignment_down,
            contentDescriptionId = R.string.text_toolbar_icon_alignment_bottom_description
        )

        AlignmentArrowIcon(
            isEnabled = modelVerticalTextAlignment != Alignment.Center,
            backgroundColor = getButtonBackgroundColor(
                isButtonPressed = modelVerticalTextAlignment == Alignment.Center
            ),
            onClick = {
                onEvent(TextToolbarEvent.VerticalTextAlignment(alignment = Alignment.Center))
            },
            resourceId = R.drawable.ic_arrows_alignment_center,
            contentDescriptionId = R.string.text_toolbar_icon_alignment_center_description
        )
    }
}

@Composable
fun AlignmentArrowIcon(
    isEnabled: Boolean,
    backgroundColor: Color,
    onClick: () -> Unit,
    @DrawableRes resourceId: Int,
    contentDescriptionId: Int,
) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = stringResource(contentDescriptionId),
        modifier = Modifier
            .size(32.dp)
            .background(color = backgroundColor)
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
    )
}

@Composable
fun SetDivider(horizontal: Dp) {
    Icon(
        painter = painterResource(id = R.drawable.ic_text_toolbar_divider),
        contentDescription = stringResource(R.string.note_toolbar_icon_divider_description),
        modifier = Modifier.padding(horizontal = horizontal)
    )
}

@Composable
fun SetDividerTextSizePart(start: Dp, end: Dp) {
    Icon(
        painter = painterResource(id = R.drawable.ic_text_toolbar_divider),
        contentDescription = stringResource(R.string.note_toolbar_icon_divider_description),
        modifier = Modifier.padding(start = start, end = end)
    )
}

@Composable
fun SetBoldText(styleState: TextStyleState, onEvent: (TextToolbarEvent) -> Unit) {
    Text(
        text = "B",
        style = TextStyle(
            fontSize = buttonTextSize,
            fontWeight = FontWeight.Bold,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .size(buttonInternalPadding)
            .clickable {
                onEvent(
                    TextToolbarEvent.TextStyleChange(
                        styleState = styleState.copy(isBold = !styleState.isBold)
                    )
                )
            }
            .background(
                color = getButtonBackgroundColor(styleState.isBold)
            )
    )
}

@Composable
fun SetItalicText(styleState: TextStyleState, onEvent: (TextToolbarEvent) -> Unit) {
    Text(
        text = "I",
        style = TextStyle(
            fontSize = buttonTextSize,
            fontFamily = FontFamily(Font(R.font.caladea_regular)),
            fontStyle = FontStyle.Italic,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .size(buttonInternalPadding)
            .clickable {
                onEvent(
                    TextToolbarEvent.TextStyleChange(
                        styleState = styleState.copy(isItalic = !styleState.isItalic)
                    )
                )
            }
            .background(
                color = getButtonBackgroundColor(styleState.isItalic)
            )
    )
}

@Composable
fun SetUnderlineText(styleState: TextStyleState, onEvent: (TextToolbarEvent) -> Unit) {
    Text(
        text = "U",
        style = TextStyle(
            fontSize = buttonTextSize,
            textDecoration = TextDecoration.Underline,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .size(buttonInternalPadding)
            .clickable {
                onEvent(
                    TextToolbarEvent.TextStyleChange(
                        styleState = styleState.copy(isUnderlined = !styleState.isUnderlined)
                    )
                )
            }
            .background(
                color = getButtonBackgroundColor(styleState.isUnderlined)
            )
    )
}

@Composable
fun SetStrikeThroughText(styleState: TextStyleState, onEvent: (TextToolbarEvent) -> Unit) {
    Text(
        text = "S",
        style = TextStyle(
            fontSize = buttonTextSize,
            textDecoration = TextDecoration.LineThrough,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .size(buttonInternalPadding)
            .clickable {
                onEvent(
                    TextToolbarEvent.TextStyleChange(
                        styleState = styleState.copy(isLineThrough = !styleState.isLineThrough)
                    )
                )
            }
            .background(
                color = getButtonBackgroundColor(styleState.isLineThrough)
            )
    )
}

private fun getButtonBackgroundColor(
    isButtonPressed: Boolean,
): Color = if (isButtonPressed) Color.LightGray else Color.Transparent