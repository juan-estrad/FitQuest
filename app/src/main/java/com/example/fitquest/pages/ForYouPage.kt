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
                            WeeklyBox(
                                name = " ",
                                title = "My Title",
                                text = "This is some text in the box.",
                                workouts = " ",
                                onClick = {

                                }
                            )
                            WeeklyBox(
                                name = " ",

                                title = "My Title",
                                text = "This is some text in the box.",
                                workouts = " ",
                                onClick = {

                                }
                            )
                            WeeklyBox(
                                name = " ",
                                title = "My Title",
                                text = "This is some text in the box.",
                                workouts = " ",
                                onClick = {

                                }
                            )
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
fun DailyBox(name: String, title: String, text: String, workouts: String, onClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val database = FirebaseDatabase.getInstance()
    val userID = FirebaseAuth.getInstance().uid
    val time :Long = System.currentTimeMillis()
    val formatMonthDay : SimpleDateFormat = SimpleDateFormat("M-dd", Locale.getDefault())
    val formatYear : SimpleDateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
    val monthday :String= formatMonthDay.format(time)
    val year :String= formatYear.format(time)
    val referenceWorkoutTitle = database.getReference("workouts")
    var childValue1 by remember { mutableStateOf("") }
    var childValue2 by remember { mutableStateOf("") }
    var childValue3 by remember { mutableStateOf("") }

    val ref1 = database.getReference("workouts/$childValue1")
    val ref2 = database.getReference("workouts/$childValue1/$childValue2")

    LaunchedEffect(Unit) {
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
            Text(text =  childValue2,)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description:",
                fontSize = 14.sp)
            Text(text = childValue3,
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineSmall,
                lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Focus: ",
                    fontSize = 14.sp)
                Text(text = childValue1,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.headlineSmall)
                Button(onClick = { }) { }
            }
        }
    }
}

@Composable
fun ModalButtonExample() {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Show Modal Dialog")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Modal Dialog Title") },
            text = { Text("This is a modal dialog.") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}



fun getRandomChild(databaseReference: DatabaseReference) {
    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val children = snapshot.children.toList()
            if (children.isNotEmpty()) {
                val randomIndex = Random.nextInt(children.size)
                val randomChild = children[randomIndex]

                // Do something with the random child
                println(randomChild.key) // Key of the random child
                println(randomChild.value) // Value of the random child
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
            println("Error: ${error.message}")
        }
    })
}

@Composable
fun RandomChildDisplay() {
    val database = FirebaseDatabase.getInstance()
    val userID = FirebaseAuth.getInstance().uid
    val time :Long = System.currentTimeMillis()
    val formatMonthDay : SimpleDateFormat = SimpleDateFormat("M-dd", Locale.getDefault())
    val formatYear : SimpleDateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
    val monthday :String= formatMonthDay.format(time)
    val year :String= formatYear.format(time)
    val referenceWorkout = database.getReference("workouts").child("Arms")
    var childValue by remember { mutableStateOf("") }
    var childValue2 by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        while (true) {
            val snapshot = referenceWorkout.get().await()
            val children = snapshot.children.toList()
            val randomIndex = (0 until children.size).random()
            childValue = children[randomIndex].key.toString()
            delay(24 * 60 * 60 * 1000) // 24 hours in milliseconds
        }
    }

    Text(text = childValue)
}

/*fun fetchRandomWorkout(onResult: (Workout?) -> Unit) {
    val database = FirebaseDatabase.getInstance().getReference("workouts")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val workoutList = mutableListOf<Workout?>() // Pair of (workout name, description)
            for (category in snapshot.children) {
                for (workout in category.children) {
                    val workoutName = workout.key ?: continue
                    val description = workout.child("description").getValue(String::class.java) ?: continue
                    workoutList.add(Workout)
                }
            }

            // Randomly select a workout
            if (workoutList.isNotEmpty()) {
                val randomWorkout = workoutList.random()
                onResult(randomWorkout) // Pass the selected workout as a pair
            } else {
                onResult(null) // No workouts found
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(null) // Handle errors
        }
    })
}

@Composable
fun RandomWorkoutCard() {
    var workoutName by remember { mutableStateOf<String?>(null) }
    var workoutDescription by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fetchRandomWorkout { result ->
            result?.let { (name, description) ->
                workoutName = name
                workoutDescription = description
            } ?: run {
                workoutName = "No Workout Available"
                workoutDescription = ""
            }
        }
    }

    Card(
        modifier = Modifier.padding(16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            workoutName?.let {
                Text(text = it, fontWeight = FontWeight.Bold)
            } ?: Text("Loading...")

            Spacer(modifier = Modifier.height(8.dp))

            workoutDescription?.let {
                Text(text = it)
            } ?: Text("...")
        }
    }
}

fun fetchDailyCard(onResult: (Workouts?) -> Unit) {
    val database = FirebaseDatabase.getInstance().getReference("workouts").child("Arms")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val cardList = mutableListOf<Workouts>()
            for (child in snapshot.children) {
                val card = child.getValue(Workouts::class.java)
                if (card != null) {
                    cardList.add(card)
                }
            }

            // Get the current day of the month and use it to select a card
            val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val index = (currentDay - 1) % cardList.size // Using modulo to wrap around
            onResult(cardList.getOrNull(index))
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(null) // Handle errors
        }
    })
}

@Composable
fun DailyCard() {
    var cardContent by remember { mutableStateOf<Workouts?>(null) }
    val referenceWorkoutTitle = database.getReference("workouts")
    var childValue1 by remember { mutableStateOf("") }
    var childValue2 by remember { mutableStateOf("") }
    var childValue3 by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchDailyCard { content ->
            cardContent = content
        }
        val snapshot1 = referenceWorkoutTitle.get().await()

        childValue1 = snapshot1.key.toString()
    }

    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        cardContent?.let {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = it.workoutTypes, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = childValue1)
            }
        } ?: Text("Loading...")
    }
}*/

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel = WorkoutViewModel()) {
    viewModel.loadRandomWorkout()


    Card(
        modifier = Modifier
            .padding(7.dp)
            .width(325.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Display workout details if available
            viewModel.todayWorkout.value?.let { workout ->
                Text(text = "Workout Name: ${workout.name}")
                Text(text = "Workout: ${workout.description}")
                Text(text = "Strength: +${workout.strength}")
                Text(text = "Agility: +${workout.agility}")
                Text(text = "Stamina: +${workout.stamina}")
                Text(text = "Consistency: +${workout.consistency}")
                Text(text = "Dexterity: +${workout.dexterity}")



                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.completeWorkout() }) {
                    Text(text = "Complete Workout")
                }
            } ?: Text(text = "No workout loaded.")
        }
    }
}