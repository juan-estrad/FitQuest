package com.example.fitquest.pages


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel


import com.example.fitquest.UserProfile
import com.example.fitquest.UserStats
import com.example.fitquest.UserStreak
import com.example.fitquest.ui.OrangeFilledButton
import com.example.fitquest.ui.UserInputField

import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.verticalGradientBrush

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import java.time.LocalDateTime
import kotlin.system.exitProcess


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
                        userStats = UserStats(
                            agility = 0,
                            consistency = 0,
                            dexterity = 0,
                            stamina = 0,
                            strength = 0
                        ),
//                        logging = Logging(
//                            date = Date(
//                                year = Year(
//                                    monthday = Monthday(
//                                        log = Log (
//                                            workout = "",
//                                            type = "",
//                                            sets = 0,
//                                            reps = 0,
//                                            weight = "",
//                                            workouttime = ""
//                                        )
//                                    )
//                                )
//                            )
//                        ),
                        streak = UserStreak(
                            streak = 0,
                            longestStreak = 0,
                            lastUpdate = ""
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

        Spacer(modifier = Modifier.height(30.dp))


        //Username input
        UserInputField(
            label = "USERNAME",
            value = username,
            onValueChange = { username = it }
        )



        //Email input
        UserInputField(
            label = "EMAIL",
            value = email,
            onValueChange = { email = it }
        )

        //Password input
        UserInputField(
            label = "PASSWORD",
            value = password,
            onValueChange = { password = it },
            isPassword = true
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Create account button
        OrangeFilledButton("Create Account", {authViewModel.signup(email, password)},  authState.value != AuthState.Loading)


//        Button(
//            onClick = { authViewModel.signup(email, password) },
//            enabled = authState.value != AuthState.Loading
//        ) {
//            Text(text = "Create Account")
//        }
//


        Spacer(modifier = Modifier.height(16.dp))

        // Login navigation
        TextButton(onClick = { navController.navigate("login") }) {
            Text(text = "Already have an account, Login")
        }
    }


}

