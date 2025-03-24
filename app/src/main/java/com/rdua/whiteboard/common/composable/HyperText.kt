package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.rdua.whiteboard.ui.theme.MaterialTheme

private const val ACTION_TAG = "action"

@Composable
fun HyperText(
    content: List<HyperText>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    hyperTextColor: Color = MaterialTheme.colors.primaryVariant,
    hyperTextStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.SemiBold,
    )
) {
    val annotatedText = buildAnnotatedString(
        content = content,
        hyperTextStyle = hyperTextStyle.copy(color = hyperTextColor).toSpanStyle()
    )

    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        ClickableText(
            text = annotatedText,
            style = textStyle,
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = ACTION_TAG, start = offset, end = offset
                ).firstOrNull()?.let { annotation ->
                    val index = annotation.item.toInt()

                    val link = content[index]
                    if (link is HyperText.Link) {
                        link.onClick(annotation.item)
                    }
                }
            })
    }
}

private fun buildAnnotatedString(
    content: List<HyperText>,
    hyperTextStyle: SpanStyle,
): AnnotatedString {
    return buildAnnotatedString {
        content.forEachIndexed { index, hyperText ->
            when (hyperText) {
                is HyperText.Text -> {
                    append(hyperText.text)
                }

                is HyperText.Link -> {
                    val startIndex = this.length
                    val endIndex = startIndex + hyperText.text.length

                    addStyle(
                        style = hyperTextStyle,
                        start = startIndex,
                        end = endIndex
                    )

                    addStringAnnotation(
                        tag = ACTION_TAG,
                        annotation = index.toString(),
                        start = startIndex,
                        end = endIndex
                    )

                    append(hyperText.text)
                }
            }
        }
    }
}

sealed interface HyperText {
    class Text(val text: String) : HyperText
    class Link(val text: String, val onClick: (String) -> Unit) : HyperText

    companion object {
        val Space = Text(" ")
    }
}