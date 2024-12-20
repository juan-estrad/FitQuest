package com.example.fitquest.pages

import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.R
import com.example.fitquest.UserProfile
import com.example.fitquest.WeeklyWorkoutViewModel
import com.example.fitquest.WorkoutViewModel
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.brightOrange
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import androidx.compose.material3.Text as Text

///////////////////////////////Code: Alexis, Nick, Campbell, Joseph, Juan and Tanner////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, workoutViewModel: WorkoutViewModel, weeklyWorkoutViewModel: WeeklyWorkoutViewModel) {
    TopAndBottomAppBar(
        contents = { ForYouPageContents(modifier,navController,authViewModel, workoutViewModel, weeklyWorkoutViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )
}

@Composable
fun ForYouPageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, workoutViewModel: WorkoutViewModel, weeklyWorkoutViewModel: WeeklyWorkoutViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    LaunchedEffect(authState.value) {
        when (authState.value) {
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
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 0.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
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
                            WorkoutScreen1(workoutViewModel, profile)
                            WorkoutScreen2(workoutViewModel, profile)
                            WorkoutScreen3(workoutViewModel, profile)
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
                            WeeklyWorkoutScreen1(weeklyWorkoutViewModel, profile)
                            WeeklyWorkoutScreen2(weeklyWorkoutViewModel, profile)
                            WeeklyWorkoutScreen3(weeklyWorkoutViewModel, profile)
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
                        img = R.drawable.profile_1,
                        name = "Arnold Beefcake",
                        title = "T-800 (muscles only!!)",
                        text = "This is the Arnold Excercise Routine.",
                        workouts1 = "Upperbody",
                        workouts1prog = "Bench Press: 5x10 \n Bicep Curls: 5x10 \n Tricep curls: 5x10",
                        workouts2 = "Lowerbody",
                        workouts2prog = "Squats: 5x10 \n Lunges: 5x10 \n Calf Raises: 5x10",
                        workouts3 = "",
                        workouts3prog = "",
                    )
                    ReccomendedBox(
                        img = R.drawable.profile_2,
                        name = "Ronnie Coleman",
                        title = "Ronnie's gym Time!",
                        text = "The Ronnie core workout Routine",
                        workouts1 = "Core workout",
                        workouts1prog = "Bicycle Crunches: 3x8 \nLeg Raises: 3x8 \nPlanks: 5 minutes",
                        workouts2 = "Cardio",
                        workouts2prog = "Jumping Jacks: 3x5 minutes \nMountain Climbers: 3x5 minutes \nRunning: 1.5 miles",
                        workouts3 = "",
                        workouts3prog = "",
                    )
                    ReccomendedBox(
                        img = R.drawable.profile_3,
                        name = "David Goggins",
                        title = "Training to Battle Psycho's",
                        text = "The David Goggins easy routine",
                        workouts1 = "Core workout",
                        workouts1prog = "Crunches: 5x10 \nSit-Ups: 5x10 \nLeg Raises: 5x10",
                        workouts2 = "Arms",
                        workouts2prog = "Skull Crushers: 5x10 \nOverhead Tricep Extensions: 5x10 \nPreacher Curls: 5x10",
                        workouts3 = "Chest",
                        workouts3prog = "Bench Press: 5x10 \nDumbbell Flyers: 5x10 \nIncline Bench Press: 5x10",
                    )
                }
            }
        }
    }
}


@Composable
fun ReccomendedBox(img: Int, name: String, title: String, text: String, workouts1: String, workouts1prog: String, workouts2: String, workouts2prog: String,workouts3: String, workouts3prog: String) {
    var showDialog by remember { mutableStateOf(false) }
    var isButtonVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val timer = object : CountDownTimer(24 * 60 * 60 * 1000, 1000) {
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
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_2),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
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
fun WorkoutScreen1(workoutViewModel: WorkoutViewModel, profile: UserProfile) {
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    workoutViewModel.loadUserChallenges(profile)
    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////////////////////////////////workout 1////////////////////////////
            workoutViewModel.todayWorkout.value = profile.challenges.dailyChallenge.workout1
            workoutViewModel.todayWorkout.value?.let { workout ->
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
                Button(onClick = {
                    workoutViewModel.completeWorkout(profile)
                    profile.challenges.dailyChallenge.completeWorkout1 = true
                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout1")
                        .setValue(true)
                },
                    enabled = !profile.challenges.dailyChallenge.completeWorkout1
                    ) {
                    Text(text = "Complete Workout")

                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}

@Composable
fun WorkoutScreen2(workoutViewModel: WorkoutViewModel, profile: UserProfile) {
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    workoutViewModel.loadUserChallenges(profile)
    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////////////////////////////////workout 2////////////////////////////
            workoutViewModel.todayWorkout.value = profile.challenges.dailyChallenge.workout2
            workoutViewModel.todayWorkout.value?.let { workout ->
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
                Button(onClick = {
                    workoutViewModel.completeWorkout(profile)
                    profile.challenges.dailyChallenge.completeWorkout2 = true
                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout2")
                        .setValue(true)
                },
                    enabled = !profile.challenges.dailyChallenge.completeWorkout2
                    ) {
                    Text(text = "Complete Workout")

                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}

@Composable
fun WorkoutScreen3(workoutViewModel: WorkoutViewModel, profile: UserProfile) {
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    workoutViewModel.loadUserChallenges(profile)
    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////////////////////////////////workout 3////////////////////////////
            workoutViewModel.todayWorkout.value = profile.challenges.dailyChallenge.workout3
            workoutViewModel.todayWorkout.value?.let { workout ->
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
                Button(onClick = {
                    workoutViewModel.completeWorkout(profile)
                    profile.challenges.dailyChallenge.completeWorkout3 = true
                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout3")
                        .setValue(true)
                },
                    enabled = !profile.challenges.dailyChallenge.completeWorkout3
                    ) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}

@Composable
fun WeeklyWorkoutScreen1(weeklyWorkoutViewModel: WeeklyWorkoutViewModel, profile: UserProfile) {
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    weeklyWorkoutViewModel.loadUserChallenges(profile)
    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////////////////////////////////workout 1////////////////////////////
            weeklyWorkoutViewModel.weeklyWorkout.value = profile.challenges.weeklyChallenge.workout1
            weeklyWorkoutViewModel.weeklyWorkout.value?.let { weekly ->
                Text(text = "${weekly.name}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Workouts:",
                    fontSize = 16.sp,
                    lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "${weekly.workout1}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                    Text(text = "${weekly.workout2}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                    Text(text = "${weekly.workout2}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
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
                Button(onClick = {
                    weeklyWorkoutViewModel.completeWorkout(profile)
                    profile.challenges.weeklyChallenge.completeWorkout1 = true
                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout1")
                        .setValue(true)
                },
                    enabled = !profile.challenges.weeklyChallenge.completeWorkout1
                ) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}

@Composable
fun WeeklyWorkoutScreen2(weeklyWorkoutViewModel: WeeklyWorkoutViewModel, profile: UserProfile) {
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    weeklyWorkoutViewModel.loadUserChallenges(profile)
    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////////////////////////////////workout 2////////////////////////////
            weeklyWorkoutViewModel.weeklyWorkout.value = profile.challenges.weeklyChallenge.workout2
            weeklyWorkoutViewModel.weeklyWorkout.value?.let { weekly ->
                Text(text = "${weekly.name}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Workouts:",
                    fontSize = 16.sp,
                    lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "${weekly.workout1}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                    Text(text = "${weekly.workout2}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                    Text(text = "${weekly.workout2}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

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
                Button(onClick = {
                    weeklyWorkoutViewModel.completeWorkout(profile)
                    profile.challenges.weeklyChallenge.completeWorkout2 = true
                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout2")
                        .setValue(true)
                },
                    enabled = !profile.challenges.weeklyChallenge.completeWorkout2
                ) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}

@Composable
fun WeeklyWorkoutScreen3(weeklyWorkoutViewModel: WeeklyWorkoutViewModel, profile: UserProfile) {
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    weeklyWorkoutViewModel.loadUserChallenges(profile)
    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(350.dp)
            .height(350.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////////////////////////////////workout 3////////////////////////////
            weeklyWorkoutViewModel.weeklyWorkout.value = profile.challenges.weeklyChallenge.workout3
            weeklyWorkoutViewModel.weeklyWorkout.value?.let { weekly ->
                Text(text = "${weekly.name}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Workouts:",
                    fontSize = 16.sp,
                    lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                )
                {
                    Text(text = "${weekly.workout1}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                    Text(text = "${weekly.workout2}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                    Text(text = "${weekly.workout2}\nSets: ${weekly.Sets}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
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
                Button(onClick = {
                    weeklyWorkoutViewModel.completeWorkout(profile)
                    profile.challenges.weeklyChallenge.completeWorkout3 = true
                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout3")
                        .setValue(true)
                },
                    enabled = !profile.challenges.weeklyChallenge.completeWorkout3
                ) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}