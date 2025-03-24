package com.rdua.whiteboard.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rdua.whiteboard.R

internal object Fonts {
    val sourceSansProFont = FontFamily(
        Font(R.font.source_sans_pro_regular, FontWeight.Normal),
        Font(R.font.source_sans_pro_bold, FontWeight.Bold),
        Font(R.font.source_sans_pro_semi_bold, FontWeight.SemiBold),
        Font(R.font.source_sans_pro_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.source_sans_pro_bold_italic, FontWeight.Bold, FontStyle.Italic)
    )
}