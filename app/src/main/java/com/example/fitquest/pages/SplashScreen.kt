package com.example.fitquest.pages



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString

import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen

import com.example.fitquest.AuthViewModel
import com.example.fitquest.AuthState
import com.example.fitquest.R


import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.verticalGradientBrush


import com.example.fitquest.ui.UserInputField
import com.example.fitquest.ui.Title01
import com.example.fitquest.ui.OrangeFilledButton
import com.example.fitquest.ui.HollowOrangeButton
import com.google.firebase.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Box(
        Modifier
            .background(Color.DarkGray)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fit_quest_logo),
            contentDescription = "",
            alignment = Alignment.Center, modifier = Modifier
                .fillMaxSize().padding(40.dp)

        )

//        Text(
//            text = "Version - ${BuildConfig.VERSION_NAME}",
//            textAlign = TextAlign.Center,
//            fontSize = 24.sp,
//            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
//        )


    }
}