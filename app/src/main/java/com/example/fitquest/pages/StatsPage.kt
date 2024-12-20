package com.example.fitquest.pages

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

////////////////////////////////Code: Nick, Juan, Joseph, Alexis, Campbell, and Tanner/////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    TopAndBottomAppBar(
        false,
        contents = { StatsPageContents(modifier,navController,authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )
}

@Composable
fun StatsPageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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
                    val userRef = database.getReference("Users").child(id)
                    userRef.get().addOnSuccessListener { dataSnapshot ->
                        userProfile = dataSnapshot.getValue(UserProfile::class.java)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> Unit
        }
    }
    userProfile?.let { profile ->
        val topBottomFade = Brush.verticalGradient(0f to Color.Transparent, 0.0f to Color.Red, 0.7f to Color.Red, 1f to Color.Transparent)
        Box(
            modifier = modifier
                .size(width = 700.dp, height = 420.dp)
                .background(Color.Transparent)
                .fadingEdge(topBottomFade)
        ) {
            Image(
                painter = painterResource(id = userProfile!!.currentBackground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(),
                )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(verticalGradientBrush(darker, dark))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = userProfile!!.currentAvatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),
                    )
                Image(
                    painter = painterResource(id = userProfile!!.currentBorder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(23.dp))
            Text(
                text = profile.username,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Light,
                fontSize = 30.sp,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "STREAK  " + profile.streak.streak.toString(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Light,
                fontSize = 25.sp,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "Flexcoins: ${profile.flexcoins}",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Light,
                fontSize = 25.sp,
            )
            Spacer(modifier = Modifier.height(50.dp))
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
        }
    }
}
fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }


@Composable
fun StatItem(statName: String, statValue: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = statName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = statValue, color = Color.White, fontSize = 28.sp)
    }
}