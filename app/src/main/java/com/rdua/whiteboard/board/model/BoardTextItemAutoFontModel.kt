package com.rdua.whiteboard.board.model

/**
 * Describes a model that supports automatic font size adjustments.
 */
sealed interface BoardTextItemAutoFontModel : BoardTextItemModel {
    val isAutoFontSizeMode: Boolean
}