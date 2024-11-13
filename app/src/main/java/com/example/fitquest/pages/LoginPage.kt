package com.example.fitquest.pages

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString

import androidx.navigation.NavController
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource

import com.example.fitquest.AuthViewModel
import com.example.fitquest.AuthState
import com.example.fitquest.R


import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.verticalGradientBrush


import com.example.fitquest.ui.UserInputField
import com.example.fitquest.ui.Title01_LEFT
import com.example.fitquest.ui.OrangeFilledButton
import com.example.fitquest.ui.HollowOrangeButton
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darkOrange
import com.example.fitquest.ui.theme.transparent

///////////////////////////////Code: Alexis, Nick, Campbell, Joseph, Juan and Tanner////////////////////////////////////////////////

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_LONG
            ).show()
            else -> Unit
        }
    }
    val configuration = LocalConfiguration.current
    val iconSize = 200f
    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(verticalGradientBrush(transparent, dark)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(darkOrange, CircleShape)
                    .padding(horizontal = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fit_quest_logo),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.height( (configuration.screenHeightDp /30) .dp) )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier,
        ) {
            Text(
                text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = brightOrange)) {
                        append("Fit")
                    }
                    withStyle(style = SpanStyle(color = grayWhite)) {
                        append("Quest")
                    }
                },
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height( (configuration.screenHeightDp /30 ) .dp) )
        UserInputField(
            label = "EMAIL",
            value = email,
            onValueChange = { email = it }
        )
        UserInputField(
            label = "PASSWORD",
            value = password,
            onValueChange = { password = it },
            isPassword = true
        )
        Spacer(modifier = Modifier.height(35.dp))
        OrangeFilledButton("Login", {authViewModel.login(email, password)},  authState.value != AuthState.Loading)
        Spacer(modifier = Modifier.height( (configuration.screenHeightDp /14 ) .dp) )
        Title01_LEFT("NEED AN ACCOUNT?", grayWhite, 16f)
        HollowOrangeButton("REGISTER", {navController.navigate("signup")} ,  authState.value != AuthState.Loading)
        Spacer(modifier = Modifier.height( (configuration.screenHeightDp /12 ) .dp) )
        BackHandler {
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(homeIntent)
        }
        BackHandler(enabled = true ) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            context.startActivity(intent)
        }
    }
}



