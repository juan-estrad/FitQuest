package com.example.fitquest.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.Log
import com.example.fitquest.R
//import com.example.fitquest.Logging
import com.example.fitquest.UserProfile
import com.example.fitquest.UserStats
import com.example.fitquest.Workout
import com.example.fitquest.ui.ClickableImageWithText
import com.example.fitquest.ui.Title01
import com.example.fitquest.ui.Title01_LEFT
import com.example.fitquest.ui.TopAndBottomAppBar
//import com.example.fitquest.isStreakExpired
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darkOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.theme.verticalGradientBrush
import com.example.fitquest.updateFlexcoins
import com.example.fitquest.updateLastWorkout
import com.example.fitquest.updateStreak
//import com.example.fitquest.updateStreak
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
//    Title01("Log Your Workout", brightOrange, 32f)
//    LoggingPageContents(modifier,navController,authViewModel)

    TopAndBottomAppBar(
        false,
        contents = {
//            Box(
//                modifier = Modifier
//                    .border(7.dp, brightOrange, RoundedCornerShape(30.dp))
//                    .fillMaxSize()
////                    .fillMaxWidth()
////                    .height(100.dp)
//            ) {}

            LoggingPageContents(modifier,navController,authViewModel)
                   },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )

}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingPageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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

    var selectedText by remember { mutableStateOf("Enter Focus") }

    var selectedWorkoutType by remember { mutableStateOf("Select Workout") } // Selected workout type
    var workoutTypes by remember { mutableStateOf(listOf<String>()) } // Lis t of workout types for selected category


    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users").child(id)
                    val database =
                        Firebase.database.reference.child("workouts") // points to the Users node in firebase

                    val log = Log(
                        workout = "",
                        type = "",
                        sets = 0,
                        reps = 0,
                        weight = "",
                        workouttime = ""
                    )

                    userRef.get()
                        .addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                            userProfile = dataSnapshot.getValue(UserProfile::class.java)
                            //converts the info into a user profile object
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Failed to retrieve user data",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    database.get().addOnSuccessListener { dataSnapshot ->
                        val categories = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            it.key?.let { key -> categories.add(key) }
                        }
                        workoutCategories = categories
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch workouts", Toast.LENGTH_SHORT)
                            .show()
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


    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp

    userProfile?.let { profile ->

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .background(grayWhite)
//
//        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
//                    top = screenHeightDp.dp / 7 - 15.dp,
//                    top = paddingValues.calculateTopPadding(),
                    top = 0.dp,
                    end = 20.dp,
                    bottom = 0.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            val currentDateAndTime = sdf.format(Date())
//            Title01("Log Your Workout", brightOrange, 32f, dark)



            Box(

                modifier = Modifier
//                    .background(dark)
                    .fillMaxWidth()
                ,
                contentAlignment = Alignment.CenterStart

            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = brightOrange,
                                fontSize = (screenHeightDp / 20).sp,
                                shadow = Shadow(Color.Black)
                            )
                        )
                        {
                            append("\nWorkout Log")
                        }
//                        withStyle(
//                            style = SpanStyle(
//                                fontWeight = FontWeight.Bold,
//                                color = grayWhite,
//                                fontSize = (screenHeightDp / 5).sp,
//                                shadow = Shadow(Color.Black)
//                            )
//                        ) {
//                            append("\n_\n" )
//                        }

                        append("\n\n" )
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = grayWhite,
                                fontSize = (screenHeightDp / 20).sp,
                                shadow = Shadow(Color.Black)
                            )
                        ) {
                            append( currentDateAndTime + "\n" )
                        }

                    },
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                .background(grayWhite)

                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Spacer(modifier = Modifier.height( (screenHeightDp / 25).dp ) )



                Row {

                    val shape = if (expanded) RoundedCornerShape(8.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
                    else RoundedCornerShape(8.dp)
                    // This is the dropdown menu to select focus ///
                    Box(
                        modifier = Modifier
//                            .border(2.dp, dark, RoundedCornerShape(12.dp))
                            .fillMaxWidth()
                        ,
                        contentAlignment = Alignment.Center

//                            .clip(RoundedCornerShape(size = 16.dp)),

//                        shape = RoundedCornerShape(size = 6.dp)
//                            .padding(32.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,

                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {

                            OutlinedTextField(

                                value = selectedText,
                                onValueChange = { },
                                singleLine = true,
                                readOnly = true,
                                trailingIcon =
                                {
                                    Icon(
                                        imageVector =
                                        if (expanded) {
                                            Icons.Filled.KeyboardArrowDown
                                        }
                                        else{
                                            Icons.Filled.KeyboardArrowLeft
                                        }
                                        ,
                                        contentDescription = null,
//                                        tint = LocalContentColor.current.copy(alpha = if (expanded) ContentAlpha.high else ContentAlpha.medium),
                                        modifier = Modifier.size(50.dp)
                                    )// Adjust the size as needed
                                },

                                colors = TextFieldDefaults.outlinedTextFieldColors(

                                    focusedBorderColor = brightOrange,
                                    containerColor = darker,
                                    unfocusedBorderColor = dark,

                                ),
                                shape = RoundedCornerShape(size = 20.dp),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = (screenHeightDp / 30).sp, // Change this to your desired text size
                                    fontStyle = FontStyle.Italic,
                                    color = dark

                                ),

                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .height((screenHeightDp / 12).dp)
                            )
                            ExposedDropdownMenu(
                                modifier = Modifier
                                    .border(1.dp, dark, RoundedCornerShape(size = 20.dp))
                                    .background(
                                        color = darker,
                                        shape = RoundedCornerShape(size = 10.dp)
                                    )
//                                    .clip(RoundedCornerShape(size = 26.dp))
                                ,
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            )
                            {
                                workoutCategories.forEach { category ->

                                    Box(
                                        modifier = Modifier
                                            .height((screenHeightDp / 15f).dp)
                                            .border(1.dp, dark, RoundedCornerShape(size = 5.dp))
//                                            .background(transparent, RoundedCornerShape(size = 26.dp))
//                                                    .padding(30.dp)

                                        ,
                                        contentAlignment = Alignment.CenterStart

                                    ) {
//                                        Spacer(modifier = Modifier.height(10.dp))
                                        DropdownMenuItem(
//                                        modifier = Modifier.padding(30.dp),
                                            text = {
//                                            Text(text = category)

                                                Title01_LEFT(
                                                    "    $category",
                                                    grayWhite,
                                                    (screenHeightDp / 28f)
                                                )
                                            },
                                            onClick = {
                                                selectedText = category
                                                expanded = false
                                                Toast.makeText(
                                                    context,
                                                    category,
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()

                                            },
                                        )
                                    }
                                }

                            }
                        }



                    }


                }

                val time: Long = System.currentTimeMillis()
                val formatMonthDay: SimpleDateFormat =
                    SimpleDateFormat("M-dd", Locale.getDefault())
                val formatYear: SimpleDateFormat =
                    SimpleDateFormat("YYYY", Locale.getDefault())
                val monthday: String = formatMonthDay.format(time)
                val year: String = formatYear.format(time)




                Spacer(modifier = Modifier.height(25.dp))


                Row {

                    if (selectedText != "Cardio") {
                        Column {
//                                Spacer(modifier = Modifier.height(70.dp))





                            // This is the dropdown menu to select workout
                            ExposedDropdownMenuBox(
                                expanded = expandedWorkout,
                                onExpandedChange = { expandedWorkout = !expandedWorkout }
                            ) {

                                OutlinedTextField(

                                    value = selectedWorkoutType,
                                    onValueChange = { },
                                    singleLine = true,
                                    readOnly = true,
                                    trailingIcon =
                                    {
                                        Icon(
                                            imageVector =
                                            if (expandedWorkout) {
                                                Icons.Filled.KeyboardArrowDown
                                            }
                                            else{
                                                Icons.Filled.KeyboardArrowLeft
                                            }
                                            ,
                                            contentDescription = null,
//                                        tint = LocalContentColor.current.copy(alpha = if (expanded) ContentAlpha.high else ContentAlpha.medium),
                                            modifier = Modifier.size(50.dp)
                                        )// Adjust the size as needed
                                    },

                                    colors = TextFieldDefaults.outlinedTextFieldColors(

                                        focusedBorderColor = brightOrange,
                                        containerColor = darker,
                                        unfocusedBorderColor = dark,

                                        ),
                                    shape = RoundedCornerShape(size = 20.dp),
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = (screenHeightDp / 30).sp, // Change this to your desired text size
                                        fontStyle = FontStyle.Italic,
                                        color = dark

                                    ),

                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .height((screenHeightDp / 12).dp)
                                )
                                ExposedDropdownMenu(
                                    modifier = Modifier
                                        .border(1.dp, dark, RoundedCornerShape(size = 20.dp))
                                        .background(
                                            color = darker,
                                            shape = RoundedCornerShape(size = 10.dp)
                                        )
//                                    .clip(RoundedCornerShape(size = 26.dp))
                                    ,
                                    expanded = expandedWorkout,
                                    onDismissRequest = { expandedWorkout = false }
                                )
                                {
                                    workoutCategories.forEach { category ->

                                        Box(
                                            modifier = Modifier
                                                .height((screenHeightDp / 15f).dp)
                                                .border(1.dp, dark, RoundedCornerShape(size = 5.dp))
//                                            .background(transparent, RoundedCornerShape(size = 26.dp))
//                                                    .padding(30.dp)

                                            ,
                                            contentAlignment = Alignment.CenterStart

                                        ) {
//                                        Spacer(modifier = Modifier.height(10.dp))
                                            DropdownMenuItem(
//                                        modifier = Modifier.padding(30.dp),
                                                text = {
//                                            Text(text = category)

                                                    Title01_LEFT(
                                                        "    $category",
                                                        grayWhite,
                                                        (screenHeightDp / 28f)
                                                    )
                                                },
                                                onClick = {
                                                    selectedText = category
                                                    expandedWorkout = false
                                                    Toast.makeText(
                                                        context,
                                                        category,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()

                                                },
                                            )
                                        }
                                    }

                                }
                            }











//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
////                                        .padding(32.dp)
//                            ) {
//
//                                ExposedDropdownMenuBox(
//                                    expanded = expandedWorkout,
//                                    onExpandedChange = { expandedWorkout = !expandedWorkout }
//                                ) {
//                                    TextField(
//                                        value = selectedWorkoutType,
//                                        onValueChange = {},
//                                        readOnly = true,
//                                        trailingIcon = {
//                                            ExposedDropdownMenuDefaults.TrailingIcon(
//                                                expanded = expandedWorkout
//                                            )
//                                        },
//                                        modifier = Modifier.menuAnchor()
//                                    )
//
//                                    ExposedDropdownMenu(
//                                        expanded = expandedWorkout,
//                                        onDismissRequest = { expandedWorkout = false }
//                                    ) {
//                                        workoutTypes.forEach { workout ->
//                                            DropdownMenuItem(
//                                                text = { Text(text = workout) },
//                                                onClick = {
//                                                    selectedWorkoutType = workout
//                                                    expandedWorkout = false
//                                                    Toast.makeText(
//                                                        context,
//                                                        workout,
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//
//                                                }
//                                            )
//                                        }
//                                    }
//                                }
//                            }


                            Spacer(modifier = Modifier.height(25.dp))
                            LoggingInputField(
                                label = "Workout",
                                value = workout
                            ) {
                                workout = it
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            LoggingInputField(
                                label = "Number of Sets",
                                value = sets
                            ) {
                                sets = it
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            LoggingInputField(
                                label = "Number of Reps per Set",
                                value = reps
                            )
                            { reps = it }

                            Spacer(modifier = Modifier.height(15.dp))

                            // Weight
                            LoggingInputField(
                                label = "Weight",
                                value = weight
                            )
                            { weight = it }

                            Spacer(modifier = Modifier.height(15.dp))

                            // Time Elapsed
                            LoggingInputField(
                                label = "Workout Time",
                                value = workouttime
                            )
                            { workouttime = it }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    updateLastWorkout(profile, monthday)
                                    val userRef =
                                        database.getReference("Users").child("$userID")
                                            .child("logging").child("Date").child("$year")
                                            .child("$monthday")
                                            .child("workout" + profile.workoutCount)
                                    userRef.child("Workout").setValue(selectedWorkoutType)
                                    userRef.child("Type").setValue(selectedText)
                                    userRef.child("sets").setValue(sets)
                                    userRef.child("reps").setValue(reps)
                                    userRef.child("weight").setValue(weight)
                                    userRef.child("time").setValue(workouttime)
                                    //completeWorkout()
                                    count++
                                    workout = ""
                                    sets = ""
                                    reps = ""
                                    weight = ""
                                    workouttime = ""

                                    updateStreak(
                                        profile,
                                        LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                                        LocalDate.now().minusDays(1).format(
                                            DateTimeFormatter.ISO_DATE
                                        )
                                    )
                                    val userRef2 =
                                        database.getReference("Users").child("$userID")
                                    userRef2.child("lastWorkout").setValue(profile.lastWorkout)
                                    userRef2.child("workoutCount")
                                        .setValue(profile.workoutCount)
                                    userRef2.child("streak").child("streak")
                                        .setValue(profile.streak.streak)
                                    userRef2.child("streak").child("longestStreak")
                                        .setValue(profile.streak.longestStreak)
                                    userRef2.child("streak").child("lastUpdate")
                                        .setValue(profile.streak.lastUpdate)
                                    updateFlexcoins(profile)
                                    userRef2.child("flexcoins").setValue(profile.flexcoins)
                                    navController.navigate("logging")
                                }
                            ) {
                                Text("Add value")
                            }
                        }
                    }


                    else{
                        Column {
                            Spacer(modifier = Modifier.height(65.dp))
                            LoggingInputField(
                                label = "Distance",

                                value = distance
                            ) {
                                distance = it

                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            LoggingInputField(
                                label = "Time Elapsed",
                                value = timeelapsed
                            ) {
                                timeelapsed = it
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val userRef =
                                        database.getReference("Users").child("$userID")
                                            .child("logging").child("Date").child("$year")
                                            .child("$monthday")
                                            .child("workout" + profile.workoutCount)
                                    userRef.child("Time Elapsed").setValue(timeelapsed)
                                    userRef.child("Type").setValue(selectedText)
                                    userRef.child("Distance").setValue(distance)
                                    count++
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
                ) {
                    Button(
                        onClick = { navController.navigate("home") }
                    ) {
                        Text(text = "home")
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
            fontSize = 35.sp
        )
        Text(
            text = "*",
            color = Color.Red,
            fontSize = 35.sp
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
            .height(65.dp)
    )
}

fun completeWorkout() {
    val todayWorkout = mutableStateOf<Workout?>(null)
    val userProfile = mutableStateOf(UserProfile())

    todayWorkout.value?.let { workout ->

        userProfile.value = userProfile.value.copy(
            flexcoins = userProfile.value.flexcoins + 5,

            )

        // Update the user's stats in Firebase
        database.getReference("Users").child("$userID").child("flexcoins").setValue(userProfile.value.flexcoins)
            .addOnFailureListener { error ->
                // Handle error while updating user stats
                println("Error updating user stats: ${error.message}")
            }
    }
}