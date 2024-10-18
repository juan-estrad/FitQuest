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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

import com.example.fitquest.AuthViewModel
import com.example.fitquest.AuthState


import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.verticalGradientBrush


import com.example.fitquest.ui.UserInputField
import com.example.fitquest.ui.Title01
import com.example.fitquest.ui.OrangeFilledButton
import com.example.fitquest.ui.HollowOrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Handle authentication state changes
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







    // Login page layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(verticalGradientBrush)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        // Title
        Text(
            text =
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 35.sp)) {
                    append("Time for a \n\n\n")
                }
                withStyle(style = SpanStyle(color = brightOrange)) {
                    append("Fit")
                }
                withStyle(style = SpanStyle(color = grayWhite)) {
                    append("Quest!")
                }
            },
            fontSize = 68.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(66.dp))


        // Email input
        UserInputField(
            label = "EMAIL",
            value = email,
            onValueChange = { email = it }
        )
        


       // Password Input
        UserInputField(
            label = "PASSWORD",
            value = password,
            onValueChange = { password = it },

            isPassword = true

        )

        Spacer(modifier = Modifier.height(35.dp))

        // Login button
        OrangeFilledButton("Login", {authViewModel.login(email, password)},  authState.value != AuthState.Loading)


        Spacer(modifier = Modifier.height(32.dp))

        Title01("NEED AN ACCOUNT?")


        // Signup navigation text
        HollowOrangeButton("REGISTER", {navController.navigate("signup")} ,  authState.value != AuthState.Loading)

//        Button(
//            onClick = { navController.navigate("signup") },
//            colors = ButtonDefaults.buttonColors(containerColor = transparent),
//            enabled = authState.value != AuthState.Loading,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(55.dp),
////                .border(width = 5.dp, color = Color(0xFFD58D18)),
//            shape = RoundedCornerShape(size = 25.dp),
//            border = BorderStroke(4.5.dp, brightOrange)
//
//        ) {
//            Text(text = "REGISTER", color = brightOrange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//        }
//


    }

}



