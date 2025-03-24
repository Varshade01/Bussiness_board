package com.rdua.whiteboard.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class Colors(
    val primary: Color,
    val primaryVariant: Color,
    val background: Color,
    val textVariant: Color,
    val textAccent: Color,
    val textFaint: Color,
    val textFieldAccent: Color,
    val statusBar: Color,
    val searchTextFieldContainer: Color,
    val searchTextFieldPlaceholder: Color,
    val boardsBackground: Color,
    val emptyBoard: Color,
    val iconButton: Color,
    val textHighlighted: Color,
    val primaryBorder: Color,
    val secondaryBorder: Color,
)

fun defaultColors(
    primary: Color = Blue70,
    primaryVariant: Color = Blue60,
    background: Color = Color.White,
    textVariant: Color = Gray30,
    textAccent: Color = Color.White,
    textFaint: Color = Gray50,
    textFieldAccent: Color = Color.Black,
    statusBar: Color = Color.Transparent,
    searchTextFieldContainer: Color = GreyPurpleAlpha12,
    searchTextFieldPlaceholder: Color = DarkPurpleAlpha60,
    boardsBackground: Color = LightGrey99,
    emptyBoard: Color = BlueAlpha10,
    iconButton: Color = WhiteBlue80,
    textHighlighted: Color = Blue70,
    primaryBorder: Color = Blue50,
    secondaryBorder: Color = Gray30,
) = Colors(
    primary = primary,
    primaryVariant = primaryVariant,
    background = background,
    textVariant = textVariant,
    textAccent = textAccent,
    textFaint = textFaint,
    textFieldAccent = textFieldAccent,
    statusBar = statusBar,
    searchTextFieldContainer = searchTextFieldContainer,
    searchTextFieldPlaceholder = searchTextFieldPlaceholder,
    boardsBackground = boardsBackground,
    emptyBoard = emptyBoard,
    iconButton = iconButton,
    textHighlighted = textHighlighted,
    primaryBorder = primaryBorder,
    secondaryBorder = secondaryBorder,
)

internal val LocalColors = staticCompositionLocalOf { defaultColors() }