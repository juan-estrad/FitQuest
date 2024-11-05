package com.example.fitquest.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.UserProfile
import com.example.fitquest.WeeklyWorkoutViewModel
import com.example.fitquest.WorkoutViewModel
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.brightOrange
//import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.Text as Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
        contents = { ForYouPageContents(modifier,navController,authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouPageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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
//                .background(Color.DarkGray)
                .padding(
                    start = 16.dp,
//                    top = screenHeightDp.dp / 7 - 15.dp,
//                    top = paddingValues.calculateTopPadding(),
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 0.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                Text("FitQuest", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
//
//
//                //Plan is to make the circle the pfp but for now i just put the username in there
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .background(Color.Gray),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(profile.username, fontSize = 20.sp, color = Color.White) //profile username
//                }
//            }

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
            val database = Firebase.database
            val myRef = database.getReference("Users")


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
                            WorkoutScreen()
                            WorkoutScreen()
                            WorkoutScreen()
                        }
                    }

                }
                Row (
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text("Weekly", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Column {
                    LazyRow {
                        items(1) { index ->
                            WeeklyWorkoutScreen()
                            WeeklyWorkoutScreen()
                            WeeklyWorkoutScreen()
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                }
                Column {
                    ReccomendedBox(
                        name = "Arnold Beefcake",

                        title = "T-800 (muscles only!!)",
                        text = "This is some text in the box.",
                        workouts = "Upperbody, Lowerbody",
                        onClick = {

                        }
                    )
                    ReccomendedBox(
                        name = "Emily LoveCraft",
                        title = "Emily's gym Time!",
                        text = "Test Test Test",
                        workouts = "Core workout, Cardio",
                        onClick = {

                        }
                    )
                    ReccomendedBox(
                        name = "Dave Rubin",
                        title = "Training to Battle Psycho",
                        text = "Test",
                        workouts = "Core workout, Calisthenics, Stretching",
                        onClick = {
                        }
                    )
                }
                //RandomChildDisplay()
            }
        }
    }
}


@Composable
fun ReccomendedBox(name: String, title: String, text: String, workouts: String, onClick:  () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
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
                Text(text = workouts,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineSmall)
            }
        }
        if (showDialog) {
            AlertDialog(
                modifier = Modifier.size(750.dp, 500.dp),
                onDismissRequest = { showDialog = false },
                title = { Text(text = title) },
                text = { Text("This is a popup message.") },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel = WorkoutViewModel()) {
    viewModel.loadRandomWorkout()


    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(325.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Display workout details if available
            viewModel.todayWorkout.value?.let { workout ->
                Text(text = "${workout.name}")

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Description:",
                    lineHeight = 12.sp)
                Text(text = "${workout.description}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 15.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(25.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "Strength: +${workout.strength}",
                        fontSize = 15.sp)
                    Text(text = "Agility: +${workout.agility}",
                        fontSize = 15.sp)
                    Text(text = "Stamina: +${workout.stamina}",
                        fontSize = 15.sp)
                }

                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "Consistency: +${workout.consistency}",
                        fontSize = 15.sp)
                    Text(text = "Dexterity: +${workout.dexterity}",
                        fontSize = 15.sp)
                }



                Spacer(modifier = Modifier.height(25.dp))

                Button(onClick = { viewModel.completeWorkout() }) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}

@Composable
fun WeeklyWorkoutScreen(viewModel: WeeklyWorkoutViewModel = WeeklyWorkoutViewModel()) {
    viewModel.loadRandomWorkout()


    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Display workout details if available
            viewModel.weeklyWorkout.value?.let { weekly ->
                Text(text = "${weekly.name}")

                Text(text = "${weekly.workout1}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 14.sp)
                Text(text = "${weekly.workout2}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 14.sp)
                Text(text = "${weekly.workout3} Sets: ${weekly.Sets}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 14.sp)

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "Strength: +${weekly.strength}",
                        fontSize = 14.sp)
                    Text(text = "Agility: +${weekly.agility}",
                        fontSize = 14.sp)
                    Text(text = "Stamina: +${weekly.stamina}",
                        fontSize = 14.sp)
                }

                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "Consistency: +${weekly.consistency}",
                        fontSize = 14.sp)
                    Text(text = "Dexterity: +${weekly.dexterity}",
                        fontSize = 14.sp)
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.completeWorkout() }) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}