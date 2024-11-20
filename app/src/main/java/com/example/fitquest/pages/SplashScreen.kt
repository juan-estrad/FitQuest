package com.example.fitquest.pages

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.verticalGradientBrush
import kotlinx.coroutines.delay
import kotlin.random.Random

///////////////////////////////////////////////////Code: Nick////////////////////////////////////////////////////


@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var isDelayDone by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FadeInBrush(verticalGradientBrush(transparent, dark))),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {}
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val stringList = listOf(
            "Leg day?",
            "LET's " +
                    "\nGOOOOOOO!",
            "Start of the day",
            "YOU GOT THIS!" + "\nYOU GOT THIS!" + "\nYOU GOT THIS!",
            "Looking good!",
            "Let's pump those" +
                    "\nMUSCLES!",
            "KEEP ON\nKEEPING ON")
        RandomStringPicker(stringList)
    }
    BackHandler(enabled = true ) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)
    }
    LaunchedEffect(Unit) {
        delay(2000)
        isDelayDone = true
    }
    if(isDelayDone) {
        LaunchedEffect(key1 = true) {
            when (authState.value) {
                is AuthState.Authenticated -> navController.navigate("home")
                is AuthState.Error -> Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                else -> navController.navigate("login")
            }
        }
    }
}

@Composable
fun floatSlide(startNumber: Float, endNumber: Float) : Float {
    var target by remember { mutableFloatStateOf(startNumber) }

    val floatMove by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(
            durationMillis = 1000
        ), label = ""
    )
    LaunchedEffect(Unit) {
        target = endNumber
        delay(6000)
    }
    return floatMove
}


@Composable
fun TextAnimated(text: String, textColor: Color, fontSize: Float, startOffset: Float, endOffset: Float) {
    var offset by remember { mutableStateOf(startOffset.dp) }
    val animatedOffset by animateDpAsState(
        targetValue = offset,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(Unit) {
        offset = endOffset.dp
        delay(10000) // Delay for the animation duration
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier


    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            color = textColor,
            modifier = Modifier.offset(y = animatedOffset),
            style = LocalTextStyle.current.merge(
                TextStyle(
                    lineHeight = 50.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                )
            )
        )
    }

}

@Composable
fun RandomStringPicker(strings: List<String>) {
    val randomIndex = remember { Random.nextInt(strings.size) }
    val randomString = strings[randomIndex]
    TextAnimated(randomString, brightOrange, floatSlide(18f, 38f), 200f, -90f )
}

@Composable
fun FadeInBrush(brush: Brush): Brush {
    var alpha by remember { mutableFloatStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(
            durationMillis = 2000
        ), label = ""
    )
    LaunchedEffect(Unit) {
        alpha = 1f
    }
    return Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            dark.copy(alpha = animatedAlpha)
        ),
        startY = 250f,
        endY = Float.POSITIVE_INFINITY,
    )
}