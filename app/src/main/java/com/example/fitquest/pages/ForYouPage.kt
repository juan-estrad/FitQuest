package com.example.fitquest.pages

import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.transparent
//import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.childEvents
import com.google.firebase.database.core.Context
import com.google.firebase.database.database
import com.google.firebase.database.values
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import androidx.compose.material3.Text as Text



@Composable
fun ForYouPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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

    userProfile?.let { profile ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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
                    text = profile.streak.streak.toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Daily", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }


                Column {
                    LazyRow {
                        items(1) { index ->
                            DailyBox()
                            DailyBox()
                            DailyBox()

                        }
                    }
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recommended", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                }
                Column {
                    ReccomendedBox(
                        name = "Arnold Beefcake",

                        title = "T-800 (muscles only!!)",
                        text = "This is some text in the box.",
                        workouts1 = "Upperbody",
                        workouts1prog = "Bench Press: 5x10 \n Bicep Curls: 5x10 \n Tricep curls: 5x10",
                        workouts2 = "Lowerbody",
                        workouts2prog = "Squats: 5x10 \n Lunges: 5x10 \n Calf Raises: 5x10",
                        workouts3 = "",
                        workouts3prog = "",
                        )
                    ReccomendedBox(
                        name = "Emily LoveCraft",
                        title = "Emily's gym Time!",
                        text = "Test Test Test",
                        workouts1 = "Core workout",
                        workouts1prog = "Bicycle Crunches: 3x8 \nLeg Raises: 3x8 \nPlanks: 5 minutes",
                        workouts2 = "Cardio",
                        workouts2prog = "Jumping Jacks: 3x5 minutes \nMountain Climbers: 3x5 minutes \nRunning: 1.5 miles",
                        workouts3 = "",
                        workouts3prog = "",
                        )
                    ReccomendedBox(
                        name = "Dave Rubin",
                        title = "Training to Battle Psycho",
                        text = "Test",
                        workouts1 = "Core workout",
                        workouts1prog = "Crunches: 5x10 \nSit-Ups: 5x10 \nLeg Raises: 5x10",
                        workouts2 = "Arms",
                        workouts2prog = "Skull Crushers: 5x10 \nOverhead Tricep Extensions: 5x10 \nPreacher Curls: 5x10",
                        workouts3 = "Chest",
                        workouts3prog = "Bench Press: 5x10 \nDumbbell Flyers: 5x10 \nIncline Bench Press: 5x10",
                        )
                }
                //RandomChildDisplay()
            }
        }
    }
}


@Composable
fun ReccomendedBox(name: String, title: String, text: String, workouts1: String, workouts1prog: String, workouts2: String, workouts2prog: String,workouts3: String, workouts3prog: String) {
    var showDialog by remember { mutableStateOf(false) }
    var isButtonVisible by remember { mutableStateOf(true) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid
    val agilityIncrease = database.getReference("Users").child("$userID").child("userStats").child("agility")
    val consistencyIncrease = database.getReference("Users").child("$userID").child("userStats").child("consistency")
    val dexterityIncrease = database.getReference("Users").child("$userID").child("userStats").child("dexterity")
    val staminaIncrease = database.getReference("Users").child("$userID").child("userStats").child("stamina")
    val strengthIncrease = database.getReference("Users").child("$userID").child("userStats").child("strength")


    LaunchedEffect(Unit) {
        val timer = object : CountDownTimer(24 * 60 * 60 * 1000, 1000) { // 24 hours in milliseconds
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                isButtonVisible = true
            }
        }
        timer.start()
    }

    Card(
        modifier = Modifier
            .clickable { showDialog = true  }
            .padding(7.dp)
            .width(400.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row {
                Box(modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(color = brightOrange),
                )
                Text(
                    text = "  $name",
                    fontSize = 17.sp,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(text = title)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description:",
                fontSize = 14.sp)
            Text(text = text,
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineSmall,
                lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Focus: ",
                    fontSize = 14.sp)
                Text(text = "$workouts1, $workouts2, $workouts3",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineSmall)
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = title) },
                text = {
                    if (workouts3 != "") {
                        Text(text = "Description: \n\n"
                                + "$workouts1 \n"
                                + "$workouts1prog \n\n"
                                + "$workouts2 \n"
                                + "$workouts2prog \n\n"
                                + "$workouts3 \n"
                                + "$workouts3prog"
                        )
                    }
                    else {
                        Text(text = "Description: \n\n"
                                + "$workouts1 \n"
                                + "$workouts1prog \n\n"
                                + "$workouts2 \n"
                                + "$workouts2prog"
                        )
                    }

                },
                confirmButton = {
                    if (isButtonVisible) {
                        Button(onClick = {
                            isButtonVisible = false
                            showDialog = false
                        }) {
                            Text("Click me")
                        }
                    }
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("Completed")
                    }
                }
            )
        }
    }
}

@Composable
fun WeeklyBox(name: String, title: String, text: String, workouts: String, onClick:  () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(7.dp)
            .width(325.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = title,)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description:",
                fontSize = 14.sp)
            Text(text = text,
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineSmall,
                lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Focus: ",
                    fontSize = 14.sp)
                Text(text = workouts,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
fun DailyBox() {

    var showDialog by remember { mutableStateOf(false) }
    val database = FirebaseDatabase.getInstance()
    val database1 = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    val myRef = database1.getReference("Users").child("$userID").child("userStats")

    val referenceWorkoutTitle = database.getReference("workouts")
    var childValue1 by remember { mutableStateOf("") }
    var childValue2 by remember { mutableStateOf("") }
    var childValue3 by remember { mutableStateOf("") }
    var childValue by remember { mutableStateOf("") }

    val agilityValue = database.getReference("Users").child("$userID").child("userStats").child("agility")
    val today = LocalDate.now()
    //var cardContent by remember { mutableStateOf("Content for ${today.dayOfWeek}") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var cardContent by remember { mutableStateOf("Loading...") }




    LaunchedEffect(Unit) {
        /*myRef.child("agility").get().addOnSuccessListener { dataSnapshot ->
            childValue = dataSnapshot.value.toString()
        }.addOnFailureListener { exception ->
            // Handle errors
        }*/


        while (true) {
            val snapshot1 = referenceWorkoutTitle.get().await()
            val children1 = snapshot1.children.toList()
            val randomIndex1 = (0 until children1.size).random()
            childValue1 = children1[randomIndex1].key.toString()


            val snapshot2 = referenceWorkoutTitle.child(childValue1).get().await()
            val children2 = snapshot2.children.toList()
            val randomIndex2 = (0 until children2.size).random()
            childValue2 = children2[randomIndex2].key.toString()


            val snapshot3 = referenceWorkoutTitle.child(childValue1).child(childValue2).get().await()
            val children3 = snapshot3.children.toList()
            val randomIndex3 = (0 until children3.size).random()
            childValue3 = children3[randomIndex3].value.toString()

            delay(24 * 60 * 60 * 1000) // 24 hours in milliseconds
        }
    }

    Card(
        modifier = Modifier
            .clickable { showDialog = true }
            .padding(7.dp)
            .width(325.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text =  childValue2)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description:",
                fontSize = 14.sp)
            Text(text = childValue3,
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineSmall,
                lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Focus: ",
                    fontSize = 14.sp)
                Text(text = childValue1,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.width(75.dp))
                Button(onClick = { },
                    modifier = Modifier.height(40.dp)) {
                    Text("Completed")
                }
            }
        }
    }
}



