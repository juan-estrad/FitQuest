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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.Log
import com.example.fitquest.Logging
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.TopAndBottomAppBar
//import com.example.fitquest.isStreakExpired
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.verticalGradientBrush
import com.example.fitquest.updateStreak
//import com.example.fitquest.updateStreak
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {


    TopAndBottomAppBar(
        contents = { LoggingPageContents(modifier,navController,authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel

    )



}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingPageContents(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var statname by remember {
        mutableStateOf("")
    }
    var workout by remember {
        mutableStateOf("")
    }
    var type by remember {
        mutableStateOf("")
    }
    var sets by remember {
        mutableStateOf("")
    }
    var reps by remember {
        mutableStateOf("")
    }
    var weight by remember {
        mutableStateOf("")
    }
    var workouttime by remember {
        mutableStateOf("")
    }
    var count by remember {
        mutableStateOf(0)
    }
    var distance by remember {
        mutableStateOf("")
    }
    var timeelapsed by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid
    var workoutCategories by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf(false) }
    var expandedWorkout by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf("Select a Type of Workout") }
    var selectedWorkoutType by remember { mutableStateOf("Select Workout") } // Selected workout type
    var workoutTypes by remember { mutableStateOf(listOf<String>()) } // List of workout types for selected category


    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users").child(id)
                    val database = Firebase.database.reference.child("workouts") // points to the Users node in firebase

                    val log = Log(
                        workout = "",
                        type = "",
                        sets = 0,
                        reps = 0,
                        weight = "",
                        workouttime = ""
                    )

                    userRef.get().addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                        userProfile = dataSnapshot.getValue(UserProfile::class.java)
                    //converts the info into a user profile object
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                    }
                    database.get().addOnSuccessListener { dataSnapshot ->
                        val categories = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            it.key?.let { key -> categories.add(key) }
                        }
                        workoutCategories = categories
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch workouts", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> Unit
        }
    }
    LaunchedEffect(selectedText) {
        if (selectedText.isNotEmpty()) {
            val ref = Firebase.database.reference.child("workouts").child(selectedText)
            ref.get().addOnSuccessListener { snapshot ->
                val types = snapshot.children.mapNotNull { it.key }
                workoutTypes = types
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to load workout types", Toast.LENGTH_SHORT).show()
            }
        }
    }
    userProfile?.let { profile ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(verticalGradientBrush)
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
                Text(
                    "FitQuest",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6D00)
                )


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
            Text(
                text = "Log Your Workout",
                fontSize = 32.sp
            )


        // LOGGING STARTS HERE
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // This is the dropdown menu to select focus
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded

                        }
                    ) {
                        TextField(
                            value = selectedText,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            workoutCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category) },
                                    onClick = {
                                        selectedText = category
                                        expanded = false
                                        Toast.makeText(context, category, Toast.LENGTH_SHORT).show()

                                    }
                                )
                            }
                        }
                        val time :Long = System.currentTimeMillis()
                        val formatMonthDay : SimpleDateFormat = SimpleDateFormat("M-dd", Locale.getDefault())
                        val formatYear : SimpleDateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
                        val monthday :String= formatMonthDay.format(time)
                        val year :String= formatYear.format(time)
                        if (selectedText != "Cardio") {

                            Column {
                                Spacer(modifier = Modifier.height(70.dp))

                                // This is the dropdown menu to select workout type
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp)
                                ) {
                                    ExposedDropdownMenuBox(
                                        expanded = expandedWorkout,
                                        onExpandedChange = { expandedWorkout = !expandedWorkout }
                                    ) {
                                        TextField(
                                            value = selectedWorkoutType,
                                            onValueChange = {},
                                            readOnly = true,
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedWorkout) },
                                            modifier = Modifier.menuAnchor()
                                        )

                                        ExposedDropdownMenu(
                                            expanded = expandedWorkout,
                                            onDismissRequest = { expandedWorkout = false }
                                        ) {
                                            workoutTypes.forEach { workout ->
                                                DropdownMenuItem(
                                                    text = { Text(text = workout) },
                                                    onClick = {
                                                        selectedWorkoutType = workout
                                                        expandedWorkout = false
                                                        Toast.makeText(context, workout, Toast.LENGTH_SHORT).show()

                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(70.dp))
//                                LoggingInputField(
//                                label = "Workout",
//                                value = workout) { workout = it
//                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                LoggingInputField(
                                    label = "Number of Sets",
                                    value = sets) {
                                    sets = it
                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                LoggingInputField(
                                    label = "Number of Reps per Set",
                                    value = reps)
                                { reps = it }

                                Spacer(modifier = Modifier.height(15.dp))

                                // Weight
                                LoggingInputField(
                                    label = "Weight",
                                    value = weight)
                                { weight = it }

                                Spacer(modifier = Modifier.height(15.dp))

                                // Time Elapsed
                                LoggingInputField(
                                    label = "Workout Time",
                                    value = workouttime)
                                { workouttime = it }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        val userRef = database.getReference("Users").child("$userID").child("logging").child("Date").child("$year").child("$monthday").child("workout" + count)
                                        userRef.child("Workout").setValue(workout)
                                        userRef.child("Type").setValue(selectedText)
                                        userRef.child("sets").setValue(sets)
                                        userRef.child("reps").setValue(reps)
                                        userRef.child("weight").setValue(weight)
                                        userRef.child("time").setValue(workouttime)
                                        userRef.child("time")
                                        count ++
                                        workout = ""
                                        sets = ""
                                        reps = ""
                                        weight = ""
                                        workouttime = ""

                                        //updateStreak(profile)
                                        updateStreak(profile, LocalDate.now().format(DateTimeFormatter.ISO_DATE), LocalDate.now().minusDays(1).format(
                                            DateTimeFormatter.ISO_DATE))
                                        val userRef2 = database.getReference("Users").child("$userID")
                                        userRef2.child("streak").child("streak").setValue(profile.streak.streak)
                                        userRef2.child("streak").child("longestStreak").setValue(profile.streak.longestStreak)
                                        userRef2.child("streak").child("lastUpdate").setValue(profile.streak.lastUpdate)
                                        navController.navigate("logging")
                                    }
                                ) {
                                    Text("Add value")
                                }
                            }
                        }

                        else
                            Column {
                                Spacer(modifier = Modifier.height(65.dp))

                                LoggingInputField(
                                    label = "Distance",

                                    value = distance) { distance = it

                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                LoggingInputField(
                                    label = "Time Elapsed",
                                    value = timeelapsed) {
                                    timeelapsed = it
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        val userRef = database.getReference("Users").child("$userID").child("logging").child("Date").child("$year").child("$monthday").child("workout" + count)
                                        userRef.child("Time Elapsed").setValue(timeelapsed)
                                        userRef.child("Type").setValue(selectedText)
                                        userRef.child("Distance").setValue(distance)
                                        count ++
                                        type = ""
                                        distance = ""
                                        timeelapsed = ""

                                    }
                                ) {
                                    Text("Add value")
                                }
                            }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(


                        onClick = { navController.navigate("logging") },

                        ) {
                        Text(text = "Move to logging page")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        onClick = {navController.navigate("home")}
                    ){
                        Text(text= "home")
                    }
                }

            }
        }
    }
}


//FUNCTIONS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = grayWhite,
            fontSize = 13.sp
        )
        Text(
            text = "*",
            color = Color.Red,
            fontSize = 13.sp
        )
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = brightOrange, // Orange color for focused border
            unfocusedPlaceholderColor = brightOrange,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = grayWhite,
            containerColor = darker,
            unfocusedBorderColor = Color.Transparent,
            focusedSupportingTextColor = brightOrange
        ),
        shape = RoundedCornerShape(size = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Demo_ExposedDropdownMenuBox() {
//    val context = LocalContext.current
//    val database = Firebase.database.reference.child("workouts")
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf("Select a Workout") }
//    var workoutCategories by remember { mutableStateOf(listOf<String>()) }
//
//
//    LaunchedEffect(Unit) {
//        database.get().addOnSuccessListener { dataSnapshot ->
//            val categories = mutableListOf<String>()
//            dataSnapshot.children.forEach {
//                it.key?.let { key -> categories.add(key) }
//            }
//            workoutCategories = categories
//        }.addOnFailureListener {
//            Toast.makeText(context, "Failed to fetch workouts", Toast.LENGTH_SHORT).show()
//        }
//    }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(32.dp)
//    ) {
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = {
//                expanded = !expanded
//            }
//        ) {
//            TextField(
//                value = selectedText,
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor()
//            )
//
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                workoutCategories.forEach { category ->
//                    DropdownMenuItem(
//                        text = { Text(text = category) },
//                        onClick = {
//                            selectedText = category
//                            expanded = false
//                            Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
//                            when (selectedText) {
//                                "Cardio" -> {
//
//                                }
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

