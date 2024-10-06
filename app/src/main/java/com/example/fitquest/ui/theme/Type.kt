package com.example.fitquest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fitquest.R


val interFont = FontFamily(Font(R.font.inter_bold));





// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        color = grayWhite,
        fontFamily = interFont,
//        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 1.sp
    )

//            Text(
//            text = "EMAIL OR USERNAME",
//    style = TextStyle(
//        fontSize = 16.sp,
//        fontFamily = FontFamily(Font(R.font.inter)),
//        fontWeight = FontWeight(700),
//        color = Color(0xFFEEEEEE),
//    )
//)
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)