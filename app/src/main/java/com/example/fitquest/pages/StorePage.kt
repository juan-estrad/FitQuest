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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.fitquest.ui.theme.transparent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
        contents = { StorePageContents(modifier,navController,authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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
                    val userFlexCoins = userProfile?.flexcoins ?: 0
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

    userProfile?.let { profile ->
        val userFlexCoins = userProfile?.flexcoins ?: 0
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("FitQuest Store", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFCCAA00))
                Spacer(modifier = Modifier.height(12.dp))

                // Backgrounds Category
                Text("Backgrounds", fontSize = 40.sp, color = Color(0xFFB0BEC5), fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StoreItemBox("Background 1", userFlexCoins, navController, authViewModel)
                    StoreItemBox("Background 2", userFlexCoins, navController, authViewModel)
                    StoreItemBox("Background 3", userFlexCoins, navController, authViewModel)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Borders Category
                Text("Borders", fontSize = 40.sp, color = Color(0xFFB0BEC5), fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StoreItemBox("Border 1", userFlexCoins, navController, authViewModel)
                    StoreItemBox("Border 2", userFlexCoins, navController, authViewModel)
                    StoreItemBox("Border 3", userFlexCoins, navController, authViewModel)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Avatars Category
                Text("Avatars", fontSize = 40.sp, color = Color(0xFFB0BEC5), fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StoreItemBox("Avatar 1", userFlexCoins, navController, authViewModel)
                    StoreItemBox("Avatar 2", userFlexCoins, navController, authViewModel)
                    StoreItemBox("Avatar 3", userFlexCoins, navController, authViewModel)
                }
            }

            // Smaller Home Button at the Bottom
        }
    }
}

@Composable
fun StoreItemBox(title: String, userFlexCoins: Int, navController: NavController, authViewModel: AuthViewModel) {
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val showInsufficientFundsDialog = remember { mutableStateOf(false) }
    val itemCost = 100

    Box(
        modifier = Modifier
            .size(120.dp)
            .height(120.dp)
            .background(Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    if (userFlexCoins >= itemCost) {
                        showConfirmationDialog.value = true
                    } else {
                        showInsufficientFundsDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCCAA00)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            ) {
                Text("100 FlexCoins", color = Color.Black, fontSize = 12.sp)
            }
        }
    }

    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            title = { Text("Confirm Purchase") },
            text = { Text("Are you sure you want to purchase $title for 100 FlexCoins?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB58900))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmationDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (showInsufficientFundsDialog.value) {
        AlertDialog(
            onDismissRequest = { showInsufficientFundsDialog.value = false },
            title = { Text("Insufficient FlexCoins") },
            text = { Text("You do not have enough FlexCoins to purchase $title.") },
            confirmButton = {
                Button(
                    onClick = { showInsufficientFundsDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("OK", color = Color.White)
                }
            }

        )
    }
}