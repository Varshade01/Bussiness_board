package com.rdua.whiteboard.board.composable.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.data.AdjustedFontSize

/**
 * Calculates current font size and maximum font size of [text] with a provided [style] that should
 * fit inside a specified [inputFieldSize].
 *
 * @param text text to fit inside [inputFieldSize]
 * @param style [TextStyle] for the provided [text]
 * @param inputFieldSize size that [text] should occupy
 * @param minFontSize lowest boundary for the resulting font size
 * @param maxFontSize highest boundary for the resulting font size
 * @param shouldAutoAdjust specifies whether text should initially resize within the [inputFieldSize]. It will
 * adjust automatically if [text] doesn't fit inside [inputFieldSize] even if this value is `false`.
 * @param localDensity for px, dp, sp unit conversion.
 * @param fontFamilyResolver to be used to load the font.
 *
 * @return [AdjustedFontSize] providing current and maximum font sizes the [text] occupies in [inputFieldSize].
 * If [shouldAutoAdjust] is true, current font size will be raised to maximum to occupy all available size.
 */
internal fun getAdjustedTextSize(
    text: String,
    style: TextStyle,
    inputFieldSize: DpSize,
    currentFontSize: TextUnit,
    maxFontSize: TextUnit,
    minFontSize: TextUnit = DefaultTextStyles.minFontSize,
    shouldAutoAdjust: Boolean,
    localDensity: Density,
    fontFamilyResolver: FontFamily.Resolver,
): AdjustedFontSize {

    var curFontSize: TextUnit
    var maxSize: TextUnit
    var isAutoAdjustModeEnabled: Boolean

    // Calculates the size that the text will occupy withing inputFieldSize constraints.
    val calculateTextSize: (fontSize: TextUnit) -> Size = { fontSize ->
        calculateTextSize(
            text = text,
            style = style,
            fontSize = fontSize,
            inputFieldSize = inputFieldSize,
            localDensity = localDensity,
            fontFamilyResolver = fontFamilyResolver,
        )
    }

    with(localDensity) {
        // Check if AutoMode should be enabled
        isAutoAdjustModeEnabled = isAutoAdjustModeEnabled(
            currentFontSize = currentFontSize,
            inputFieldSize = inputFieldSize.toSize(),
            shouldAutoAdjust = shouldAutoAdjust,
            calculateTextSize = calculateTextSize,
        )

        // Ensure current text fits inside the inputFieldSize.
        curFontSize = calculateFontToFitInside(
            currentFontSize = currentFontSize,
            minFontSize = minFontSize,
            inputFieldSize = inputFieldSize.toSize(),
            calculateTextSize = calculateTextSize,
        )

        // Calculating maximum font size to fit in inputFieldSize.
        maxSize = calculateMaxFontToFitInside(
            currentFontSize = curFontSize,
            maxFontSize = maxFontSize,
            inputFieldSize = inputFieldSize.toSize(),
            calculateTextSize = calculateTextSize,
        )

        // When isAutoAdjustModeEnabled, curFontSize has to be set to maximum.
        if (isAutoAdjustModeEnabled) {
            curFontSize = maxSize
        }
    }

    return AdjustedFontSize(
        currentSize = curFontSize,
        maxSize = maxSize,
        autoAdjustMode = isAutoAdjustModeEnabled
    )
}

/**
 * Creates and remembers an [AdjustedFontSize] that contains information on current and maximum font
 * size of [text] with a provided [style] that should fit inside a specified [inputFieldSize].
 */
@Composable
internal fun rememberAdjustedTextSize(
    text: String,
    style: TextStyle,
    inputFieldSize: DpSize,
    isAutoFontSizeMode: Boolean,
    maxFontSize: TextUnit,
) : MutableState<AdjustedFontSize> {
    val localDensity = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current

    return remember(text, style, inputFieldSize, isAutoFontSizeMode) {
        mutableStateOf(
            getAdjustedTextSize(
                text = text,
                style = style,
                inputFieldSize = inputFieldSize,
                currentFontSize = style.fontSize,
                maxFontSize = maxFontSize,
                shouldAutoAdjust = isAutoFontSizeMode,
                localDensity = localDensity,
                fontFamilyResolver = fontFamilyResolver,
            )
        )
    }
}

/**
 * Using [calculateTextSize] function calculates the font size for the text by decrementing font size
 * staring from [currentFontSize] until the text fits inside [inputFieldSize].
 *
 * @return the font size for the text to fit inside [inputFieldSize], but no smaller than [minFontSize].
 */
private fun calculateFontToFitInside(
    currentFontSize: TextUnit,
    minFontSize: TextUnit,
    inputFieldSize: Size,
    calculateTextSize: (fontSize: TextUnit) -> Size,
): TextUnit {
    var fontSizeToTest = currentFontSize
    var currentTextSize = calculateTextSize(currentFontSize)

    while (
        currentTextSize.isBiggerThan(inputFieldSize) && fontSizeToTest >= minFontSize
    ) {
        fontSizeToTest = fontSizeToTest.value.minus(1).sp
        currentTextSize = calculateTextSize(fontSizeToTest)
    }
    return fontSizeToTest
}

/**
 * Using [calculateTextSize] function calculates the maximum font size for the text to fit inside
 * [inputFieldSize] by incrementing font size starting from [currentFontSize].
 *
 * @return the largest font size for the text to fit inside [inputFieldSize], but no larger than [maxFontSize].
 */
private fun calculateMaxFontToFitInside(
    currentFontSize: TextUnit,
    maxFontSize: TextUnit,
    inputFieldSize: Size,
    calculateTextSize: (fontSize: TextUnit) -> Size,
): TextUnit {
    var maxFont = currentFontSize
    var fontSizeToText = currentFontSize
    var currentTextSize = calculateTextSize(currentFontSize)

    while (
        currentTextSize.isSmallerThan(inputFieldSize) && fontSizeToText <= maxFontSize
    ) {
        maxFont = fontSizeToText
        fontSizeToText = fontSizeToText.value.plus(1).sp
        currentTextSize = calculateTextSize(fontSizeToText)
    }

    return maxFont
}

/**
 * Returns whether auto font adjust mode should be active. It should be active if it's currently
 * active or if text with [currentFontSize] doesn't fit inside [inputFieldSize].
 */
private fun isAutoAdjustModeEnabled(
    currentFontSize: TextUnit,
    inputFieldSize: Size,
    shouldAutoAdjust: Boolean,
    calculateTextSize: (fontSize: TextUnit) -> Size,
): Boolean {
    return shouldAutoAdjust || calculateTextSize(currentFontSize).isBiggerThan(inputFieldSize)
}

/**
 * Calculates the [Size] of the text.
 *
 * @param text the text which size is to be calculated
 * @param style the style of the text
 * @param fontSize the font size for the text. Is passed separately to easily calculate the size
 * of the text for the same [TextStyle], but with different font sizes.
 * @param inputFieldSize the [Size] that this text should not exceed. The width is used as maxWidth
 * [Constraints] to break the text to the next line.
 * @param localDensity for px, dp, sp unit conversion.
 * @param fontFamilyResolver to be used to load the font.
 *
 * @return the [Size] of the given [text] with the given [style] and [fontSize] that should fit inside
 * [inputFieldSize].
 */
private fun calculateTextSize(
    text: String,
    style: TextStyle,
    fontSize: TextUnit,
    inputFieldSize: DpSize,
    localDensity: Density,
    fontFamilyResolver: FontFamily.Resolver,
): Size {
    return with(localDensity) {
        val fitTextInParagraph: (textToFit: String) -> Paragraph =
            { textToFit ->
                Paragraph(
                    text = textToFit,
                    style = style.copy(fontSize = fontSize),
                    constraints = Constraints(maxWidth = inputFieldSize.width.roundToPx()),
                    density = localDensity,
                    fontFamilyResolver = fontFamilyResolver,
                )
            }

        actualCalculateTextSize(
            text = text,
            fitTextInParagraph = fitTextInParagraph
        )
    }
}

/**
 * Using [fitTextInParagraph] function calculates the actual size that the [text] occupies on the screen.
 *
 * With a simple Paragraph, you can calculate the proper height of the text. But when it comes to width
 * the options are:
 * 1. Using Paragraph.width - returns Int.MAX_VALUE with no maxWidth [Constraints], or the maxWidth
 * value itself.
 * 2. Iterating over each line of the Paragraph to find the biggest width.
 *
 * This function utilizes the second approach. But the caveat is that the default LineBreak strategy
 * for most Compose text elements ignores trailing spaces. So the line doesn't break if it ends on
 * a whitespace and non-space characters fits.
 * That results in the situation where line width can be *longer* that the maxWidth [Constraints], if
 * it ends on a space.
 *
 * This function trims the trailing spaces for these lines to get their "actual" width.
 *
 * @return the [Size] of the given [text].
 */
private fun actualCalculateTextSize(
    text: String,
    fitTextInParagraph: (textToFit: String) -> Paragraph,
): Size {
    return with(fitTextInParagraph(text)) {
        // Getting the largest width of the lines in the Paragraph.
        val maxLineWidth = List(lineCount) { lineIndex ->
            val endIndex = getLineEnd(lineIndex)

            // If line ends on whitespace, get width without it.
            if (text.isNotEmpty() && text[endIndex - 1] == ' ') {
                val startIndex = getLineStart(lineIndex)
                val lineText = text.substring(startIndex, endIndex).trimEnd()
                fitTextInParagraph(lineText).getLineWidth(0)
            } else {
                getLineWidth(lineIndex)
            }
        }.max()

        Size(width = maxLineWidth, height = height)
    }
}