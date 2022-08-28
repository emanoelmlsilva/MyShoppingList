package com.example.myshoppinglist.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R

// Set of Material typography styles to start with
val LatoBlack = FontFamily(Font(R.font.lato_black))
val LatoRegular = FontFamily(Font(R.font.lato_regular))
val LatoThin = FontFamily(Font(R.font.lato_thin))
val LatoBold = FontFamily(Font(R.font.lato_bold, FontWeight.Bold))

val Typography = Typography(
    defaultFontFamily = LatoRegular,
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)