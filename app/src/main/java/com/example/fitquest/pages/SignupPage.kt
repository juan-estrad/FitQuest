package com.example.fitquest.pages


import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.UserProfile
import com.example.fitquest.UserStats
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.theme.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }

    val authState =authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        val database = Firebase.database
        val myRef = database.getReference("Users")
        val userID = FirebaseAuth.getInstance().uid

        when(authState.value) {
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userProfile = UserProfile(
                        username = username,
                        flexcoins = 0,
                        streak = 0,
                        userStats = UserStats(
                            agility = 0,
                            consistency = 0,
                            dexterity = 0,
                            stamina = 0,
                            strength = 0
                        )
                    )
                    myRef.child(id).setValue(userProfile)
                }

                navController.navigate("home")
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_LONG
            ).show()

            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(verticalGradientBrush).padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Start your journey", fontSize = 32.sp )

        Spacer(modifier = Modifier.height(16.dp))

        //Username input
        SignupInputField(
            label = "USERNAME",
            value = username,
            onValueChange = { username = it }
        )

        Spacer(modifier = Modifier.height(15.dp))

        //Email input
        SignupInputField(
            label = "EMAIL",
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(15.dp))
        //Password input
        SignupInputField(
            label = "PASSWORD",
            value = password,
            onValueChange = { password = it }
            //isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Create account button
        Button(
            onClick = { authViewModel.signup(email, password) },
            enabled = authState.value != AuthState.Loading
        ) {
            Text(text = "Create Account")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Login navigation
        TextButton(onClick = { navController.navigate("login") }) {
            Text(text = "Already have an account, Login")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupInputField(
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
            fontSize = 16.sp
        )
        Text(
            text = "*",
            color = Color.Red,
            fontSize = 16.sp
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
            .height(57.dp)
    )
}