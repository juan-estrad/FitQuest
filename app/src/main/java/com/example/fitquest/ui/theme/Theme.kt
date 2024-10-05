package com.example.fitquest.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



val verticalGradientBrush = Brush.verticalGradient(
    listOf(Color(0xFF222831) , Color(0xFF393E46)),
    startY = 0.0f,
    endY = Float.POSITIVE_INFINITY
)




private val DarkColorScheme = darkColorScheme(
    primary = darkOrange,
    secondary = darkOrange,
    tertiary = darkOrange,
    onSurface = Purple40,
    onBackground = darkOrange,

    //Other default colors to override
    background = darkOrange,
    surface = darkOrange,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White
//onBackground = Color(0xFF1C1B1F),
//onSurface = Color(0xFF1C1B1F),

)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = darkOrange


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)




@Composable
fun FitQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor  -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}