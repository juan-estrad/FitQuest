package com.example.fitquest.pages

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.HomeViewModel
import com.example.fitquest.User

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    val authState=authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    val user = User()
    user.username = "username"
    user.userAgility = 20111
    user.userConsistency = 30111
    user.streak = 20
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        // Top section with title and user icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("FitQuest", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("USER", fontSize = 12.sp, color = Color.White)
            }
        }

        // XP Progress Bar
        Text("XP", color = Color.White, fontSize = 14.sp)
        LinearProgressIndicator(
            progress = 0.7f, // Replace with dynamic XP progress
            color = Color(0xFFFF6D00),
            trackColor = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(vertical = 8.dp)
        )

        // Streak Section
        Text(user.streak.toString() + " STREAK", color = Color.White, fontWeight = FontWeight.Light, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Stats Section
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
                    StatItem("Strength", "85")
                    StatItem("Consistency", user.userConsistency.toString().toString())
                    StatItem("Stamina", "90")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem("Dexterity", "65")
                    StatItem("Agility", user.userAgility.toString().toString())
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Featured Workouts Section
        Text("Featured Workouts", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Out Button
        TextButton(onClick = { authViewModel.signout() }) {
            Text(text = "Sign Out", color = Color.Red)
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

