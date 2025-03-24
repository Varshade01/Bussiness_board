package com.rdua.whiteboard.data.entities.boards.wrappers

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.annotations.AutoMap

@AutoMap
data class TextStyleEntity(
    val color: String,
    val fontSize: Float,
    val fontWeight: Int?,
    val italic: Boolean?, // fontStyle
    val underline: Boolean?, // textDecorations
    val lineThrough: Boolean?, // textDecorations
    val textAlign: String?, // horizontal alignment
)

fun TextStyle.toTextStyleEntity() = TextStyleEntity(
    color = color.toColorJson(),
    fontSize = fontSize.value,
    fontWeight = fontWeight?.weight,
    italic = fontStyle?.let { it == FontStyle.Italic },
    underline = textDecoration?.contains(TextDecoration.Underline),
    lineThrough = textDecoration?.contains(TextDecoration.LineThrough),
    textAlign = textAlign?.toJson(),
)

fun TextStyleEntity.toTextStyle() = TextStyle(
    color = color.fromColorJson()!!,
    fontSize = fontSize.sp,
    fontWeight = fontWeight?.let { FontWeight(fontWeight) },
    fontStyle = if (italic == true) FontStyle.Italic else null,
    textDecoration = createTextDecoration(underline, lineThrough),
    textAlign = textAlign?.fromTextAlignJson(),
)

private fun createTextDecoration(underline: Boolean?, lineThrough: Boolean?): TextDecoration? {
    return if (underline == true && lineThrough == true) {
        TextDecoration.Underline + TextDecoration.LineThrough
    } else if (underline == true) {
        TextDecoration.Underline
    } else if (lineThrough == true) {
        TextDecoration.LineThrough
    } else null
}