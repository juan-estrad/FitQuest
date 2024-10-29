package com.example.fitquest.pages

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {



    TopAndBottomAppBar(
    contents = { HomePageContents(modifier,navController,authViewModel) },
    modifier = modifier,
    navController = navController,
    authViewModel = authViewModel
    )

}



@Composable
fun HomePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users")
                        .child(id) // points to the Users node in firebase

                    userRef.get()
                        .addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                            userProfile =
                                dataSnapshot.getValue(UserProfile::class.java) //converts the info into a user profile object
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            else -> Unit
        }
    }


    // display content if the userProfile is not null
    userProfile?.let { profile ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(com.example.fitquest.ui.theme.verticalGradientBrush)
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STREAK",
                    color = grayWhite,
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = profile.streak.streak.toString(),
                    color = grayWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            Spacer(modifier = Modifier.height(16.dp))

            // Logging
            Button(
                onClick = { navController.navigate("logging") },
                colors = ButtonDefaults.buttonColors(containerColor = transparent),
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
//                .border(width = 5.dp, color = Color(0xFFD58D18)),
                shape = RoundedCornerShape(size = 25.dp),
                border = BorderStroke(4.5.dp, brightOrange)

            ) {
                Text(
                    text = "Log Workout",
                    color = brightOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Store
            Button(
                onClick = { navController.navigate("store") },
                colors = ButtonDefaults.buttonColors(containerColor = transparent),
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
//                .border(width = 5.dp, color = Color(0xFFD58D18)),
                shape = RoundedCornerShape(size = 25.dp),
                border = BorderStroke(4.5.dp, brightOrange)

            ) {
                Text(
                    text = "Store",
                    color = brightOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Stats
            Button(
                onClick = { navController.navigate("stats") },
                colors = ButtonDefaults.buttonColors(containerColor = transparent),
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
//                .border(width = 5.dp, color = Color(0xFFD58D18)),
                shape = RoundedCornerShape(size = 25.dp),
                border = BorderStroke(4.5.dp, brightOrange)

            ) {
                Text(
                    text = "Stats",
                    color = brightOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // For You
            Button(
                onClick = { navController.navigate("foryou") },
                colors = ButtonDefaults.buttonColors(containerColor = transparent),
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
//                .border(width = 5.dp, color = Color(0xFFD58D18)),
                shape = RoundedCornerShape(size = 25.dp),
                border = BorderStroke(4.5.dp, brightOrange)

            ) {
                Text(
                    text = "For You",
                    color = brightOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            TextButton(
                onClick = { authViewModel.signout() }) {
                Text(text = "Sign Out", color = Color.Red)
            }


        }
    }

    // This modify the device's back button to function like a the device's home button
    // pressing it will make it minimize the app in stead of shutting it down completely
    BackHandler(enabled = true) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)


        // THIS FUNCTION BELOW WILL SHUTDOWN THE APP
        // which the app has to be relaunch after

        //exitProcess(1);
    }
}

