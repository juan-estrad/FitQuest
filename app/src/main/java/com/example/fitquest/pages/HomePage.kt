package com.example.fitquest.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.R
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.ClickableImageWithText
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.darkOrange
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

///////////////////////////////Code: Alexis, Nick, Campbell, Joseph, Juan and Tanner////////////////////////////////////////////////

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    TopAndBottomAppBar(
    contents = { HomePageContents(modifier,navController,authViewModel) },
    modifier = modifier,
    navController = navController,
    authViewModel = authViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users")
                        .child(id)
                    userRef.get()
                        .addOnSuccessListener { dataSnapshot ->
                            userProfile =
                                dataSnapshot.getValue(UserProfile::class.java)
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
            else -> Unit
        }
    }
    userProfile?.let { profile ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    top = 0.dp,
                    end = 20.dp,
                    bottom = 0.dp
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.toFloat()
            Spacer(
                modifier = Modifier
                    .height( (screenHeight/36) .dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableImageWithText(
                    "Log Workout",
                    "Stats page Background",
                    {navController.navigate("logging")},
                    authState.value != AuthState.Loading,
                    R.drawable.logpagebtn
                )
            }
            Spacer(modifier = Modifier.height(23.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableImageWithText(
                    "Store",
                    "Stats page Background",
                    { navController.navigate("store") },
                    authState.value != AuthState.Loading,
                    R.drawable.storepagebtn
                )
            }
            Spacer(modifier = Modifier.height(23.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableImageWithText(
                    "Profile",
                    "Stats page Background",
                    {navController.navigate("stats")},
                    authState.value != AuthState.Loading,
                    R.drawable.statspagebtn
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableImageWithText(
                    "For You",
                    "Recommended Work Outs image",
                    { navController.navigate("foryou") },
                    authState.value != AuthState.Loading,
                    R.drawable.fypbtn
                )
            }
            Spacer(
                modifier = Modifier
                    .height( (screenHeight/32) .dp)
            )
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                    Arrangement.Start
            ){
                TextButton(
                    onClick = { authViewModel.signout() }) {
                    Text(
                        text = "Sign Out",
                        color = darkOrange,
                        textAlign = TextAlign.Left
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .height( (screenHeight/36) .dp)
            )
        }
    }
    BackHandler(enabled = true ) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)
    }
}

