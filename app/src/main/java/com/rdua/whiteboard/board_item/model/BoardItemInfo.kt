package com.rdua.whiteboard.board_item.model

/**
 * Represents board item information. Time field should be preformatted to desired format.
 */
data class BoardItemInfo(
    val createdBy: String? = null,
    val createdTime: String? = null,
    val modifiedBy: String? = null,
    val modifiedTime: String? = null,
)