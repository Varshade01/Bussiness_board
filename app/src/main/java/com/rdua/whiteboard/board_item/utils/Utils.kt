package com.rdua.whiteboard.board_item.utils

/**
 * Removes extra dots from the given string.
 * Extra dots are consecutive dots beyond the first occurrence of a dot.
 * For example:
 * - "12..4" will be transformed to "12.4"
 *
 * @return The string with extra dots removed.
 */
internal fun String.removeExtraDots(): String {
    val indexFirst = indexOf('.')
    return substring(0, indexFirst + 1) + substring(indexFirst + 1).replace(".", "")
}

/**
 * Removes decimal zero from a font size string.
 * For example, "12.0" will be transformed to "12", while "14.5" remains unchanged.
 * @return The font size string without trailing decimal zero, if present; otherwise, returns the original string.
 */
internal fun String.deleteDecimalZeroInFontSize(): String {
    val pattern = Regex("\\.0")
    return if (pattern.containsMatchIn(this)) {
        replace(pattern, "")
    } else {
        this
    }
}

/**
 * Returns a string containing the first [n] characters from this string, not counting '.' (dot) character
 * or the entire string if this string is shorter.
 * For example, for `"13.24".takeNotCountingDots(3)` will return `"13.2"`.
 */
internal fun String.takeNotCountingDots(n: Int): String {
    var count = n
    return takeWhile {
        if (it != '.') count--
        count >= 0
    }
}