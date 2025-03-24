package com.rdua.whiteboard.board_item.event

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.rdua.whiteboard.board_item.screen.toolbar.text_bar.TextStyleState

sealed interface TextToolbarEvent {
    //text decoration
    data class TextStyleChange(val styleState: TextStyleState) : TextToolbarEvent

    //changing text size
    object ScaleDownText : TextToolbarEvent
    object ScaleUpText : TextToolbarEvent

    //text aligning
    data class HorizontalTextAlign(val textAlign: TextAlign) : TextToolbarEvent
    data class VerticalTextAlignment(val alignment: Alignment) : TextToolbarEvent

    // toolbar text font color
    object ToggleFontColorBar : TextToolbarEvent
    data class SelectedFontColor(val color: Color) : TextToolbarEvent

    //toolbar control
    object ToggleTextToolbar : TextToolbarEvent

    //font size toolbar control
    data class EnterFontSize(val fontSize: String) : TextToolbarEvent
    data class ChangeFontSize(val fontSize: TextUnit) : TextToolbarEvent
    object ToggleFontSizeToolbar : TextToolbarEvent
    data class SetAutoMode(val switchAutoMode: Boolean) : TextToolbarEvent

    object UpdateFontSizeText : TextToolbarEvent
}
