package com.example.fitquest.pages

import android.R.attr.button
import android.os.Build.VERSION_CODES.N
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuItemColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.Logging
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.theme.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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

    var expanded by remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }

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

                    val logging = Logging(
                        workout = "",
                        type = "",
                        sets = 0,
                        reps = 0,
                        weight = "",
                        workouttime = ""
                    )

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
                    text = profile.streak.toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Log Your Workout",
                fontSize = 32.sp
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Workoutname
                Demo_ExposedDropdownMenuBox()
                /*fun c() {
                    var expanded by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = { /* Handle edit! */ },
                                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = { /* Handle settings! */ },
                                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Send Feedback") },
                                onClick = { /* Handle send feedback! */ },
                                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                                trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = workout,
                    onValueChange = { workout = it },
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
                )*/

                // Type of Workout
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = grayWhite)) { append("Type of Workout ") }
                            withStyle(style = SpanStyle(color = Color.Red)) { append("*") }
                        },
                        color = grayWhite,
                        textAlign = TextAlign.Left,
                        fontSize = 13.sp
                    )

                }
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    ),
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

                //Number of sets
                LoggingInputField(
                    label = "Number of Sets",
                    value = sets)
                    { sets = it }

                Spacer(modifier = Modifier.height(15.dp))

                // Number of Reps per Set
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

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val time :Long = System.currentTimeMillis()
                            val formatMonthDay : SimpleDateFormat = SimpleDateFormat("M-dd", Locale.getDefault())
                            val formatYear : SimpleDateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
                            val monthday :String= formatMonthDay.format(time)
                            val year :String= formatYear.format(time)
                            val userRef = database.getReference("Users").child("$userID").child("logging").child("Date").child("$year").child("$monthday").child("workour" + count)
                            userRef.child("Workout").setValue(workout)
                            userRef.child("Type").setValue(type)
                            userRef.child("sets").setValue(sets)
                            userRef.child("reps").setValue(reps)
                            userRef.child("weight").setValue(weight)
                            userRef.child("time").setValue(workouttime)
                            count ++
                        }
                    ) {
                        Text("Add value")
                    }
                    Button(
                        onClick = { navController.navigate("logging") },

                        ) {
                        Text(text = "Move to logging page")
                    }

                }
            }
        }
    }
}

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

@Composable
fun MenuSample() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        Button(onClick = { expanded = true }) {
            Text("Choose workout")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Cardio") },
                onClick = { /* Handle edit! */ },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = { /* Handle settings! */ },
                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                onClick = { /* Handle send feedback! */ },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox() {
    val context = LocalContext.current
    val coffeeDrinks = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }

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
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}