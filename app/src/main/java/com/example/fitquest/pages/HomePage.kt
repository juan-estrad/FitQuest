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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users").child(id) // points to the Users node in firebase

                    userRef.get().addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                        userProfile = dataSnapshot.getValue(UserProfile::class.java) //converts the info into a user profile object
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> Unit
        }
    }

    // display content if the userProfile is not null
    userProfile?.let { profile ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text("FitQuest", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))


                //Plan is to make the circle the pfp but for now i just put the username in there
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(profile.username, fontSize = 20.sp, color = Color.White) //profile username
                }
            }

//            // Display XP Progress Bar probably dont need this
//            Text("XP", color = Color.White, fontSize = 14.sp)
//            LinearProgressIndicator(
//                progress = 0.7f,
//                color = Color(0xFFFF6D00),
//                trackColor = Color.LightGray,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(8.dp)
//                    .padding(vertical = 8.dp)
//            )


            // This displays the streak
            // i think the future plan is to have a fire emoji or something around it
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STREAK",
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = profile.streak.toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        //display user stats
                        StatItem("Strength", profile.userStats.strength.toString())
                        StatItem("Consistency", profile.userStats.consistency.toString())
                        StatItem("Stamina", profile.userStats.stamina.toString())
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem("Dexterity", profile.userStats.dexterity.toString())
                        StatItem("Agility", profile.userStats.agility.toString())
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // display user flexcoin
            Text("Flexcoins: ${profile.flexcoins}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Sign Out Button
            TextButton(onClick = { authViewModel.signout() }) {
                Text(text = "Sign Out", color = Color.Red)
            }
        }
    }
}


@Composable
fun StatItem(statName: String, statValue: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = statName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = statValue, color = Color.White, fontSize = 18.sp)
    }
}