package com.example.fitquest.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthViewModel
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.fitquest.AuthState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.transparent


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

    val verticalGradientBrush = Brush.verticalGradient(
        listOf( Color.Transparent, dark),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )


    val linear = Brush.linearGradient(listOf(Color.Red, Color.Blue));


    // Login page layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(verticalGradientBrush)
            .padding(30.dp)
        ,
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
        LoginInputFields(
            label = "EMAIL",
            value = email,
            onValueChange = { email = it }
        )


        Spacer(modifier = Modifier.height(15.dp))

       // Password Input
        LoginInputFields(
            label = "PASSWORD",
            value = password,
            onValueChange = { password = it },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(45.dp))

        // Login button
        Button(
            onClick = { authViewModel.login(email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = brightOrange),
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier.fillMaxWidth().height(65.dp),
            shape = RoundedCornerShape(size = 18.dp)
        ) {
            Text(
                text = "Login",
                color = grayWhite,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        Box(modifier = Modifier
            .fillMaxWidth()

        ){
            Text(text = "NEED AN ACCOUNT?", color = grayWhite, textAlign = TextAlign.Left, fontSize = 16.sp )
        }

        // Signup navigation text
        Button(
            onClick = { navController.navigate("signup") },
            colors = ButtonDefaults.buttonColors(containerColor = transparent),
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
//                .border(width = 5.dp, color = Color(0xFFD58D18)),
            shape = RoundedCornerShape(size = 25.dp),
            border = BorderStroke(4.5.dp, brightOrange)

        ) {
            Text(text = "REGISTER", color = brightOrange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginInputFields(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment= Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = grayWhite,
            textAlign = TextAlign.Left,
            fontSize = 13.sp
        )
        Text(
            text = "*",
            color = Color.Red,
            fontSize = 13.sp
        )
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = brightOrange,
            unfocusedPlaceholderColor = brightOrange,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = grayWhite,
            containerColor = darker,
            unfocusedBorderColor = Color.Transparent,
            focusedSupportingTextColor = brightOrange
        ),
        shape = RoundedCornerShape(size = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    )
}

