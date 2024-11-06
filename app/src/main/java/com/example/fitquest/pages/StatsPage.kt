package com.example.fitquest.pages

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
//import com.example.fitquest.Date
//import com.example.fitquest.Logging
import com.example.fitquest.R
import com.example.fitquest.R.drawable.img
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.TopAndBottomAppBar
//import com.example.fitquest.Year
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.transparent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
        contents = { StatsPageContents(modifier,navController,authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsPageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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
//                .background(Color.DarkGray)
                .padding(16.dp)
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
            TextButton(
                onClick = { authViewModel.signout() }) {
                Text(text = "Sign Out", color = Color.Red)
            }
//            Button(
//                onClick = {
//                    val userRef = database.getReference("Users").child("$userID")
//                    userRef.child("userStats").child("agility").setValue(8)
//                }
//            ) {
//                Text("Test")
//            }
            Button(
                onClick = { navController.navigate("home") },
                colors = ButtonDefaults.buttonColors(containerColor = transparent),
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
//                .border(width = 5.dp, color = Color(0xFFD58D18)),
                shape = RoundedCornerShape(size = 25.dp),
                border = BorderStroke(4.5.dp, brightOrange)

            ) {
                Text(text = "HomePage", color = brightOrange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(15.dp))

            //VisibilityToggleButtons()
            //test()
            //MyScreen()
            DisplayChildrenButton()
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

@Composable
fun test() {
    val database = Firebase.database
    val childRef = database.getReference("User")
    childRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                for (child in dataSnapshot.children) {
                    // Access the child's key and value
                    val key = child.key
                    val value = child.value

                    // Process the data as needed
                    Log.d("Firebase", "Key: $key, Value: $value")
                    //Text(text ="Firebase", "Key: $key, Value: $value")
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle any errors
            Log.e("Firebase", "Error: ${databaseError.message}")
        }
    })
}

@Composable
fun VisibilityToggleButtons() {
    var showButtons by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { showButtons = !showButtons }) {
            Text(if (showButtons) "2024" else "2024")
        }

        AnimatedVisibility(visible = showButtons) {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { /* Action for Button 1 */ }) {
                        Text("Button 1")
                    }

                    Button(onClick = { /* Action for Button 2 */ }) {
                        Text("Button 2")
                    }
                }
            }
        }
    }
}

/*@Composable
fun DisplayChildrenButton() {
    var children by remember { mutableStateOf(emptyList<Map<String, Any>>()) }
    val db = Firebase.database.getInstance()

    Column {
        Button(onClick = {
            db.collection("your_collection")
                .get()
                .addOnSuccessListener { documents ->
                    children = documents.map { it.data }
                }
        }) {
            Text("Load Children")
        }

        children.forEach { child ->
            Text(
                text = "Name: ${child["name"]}, Age: ${child["age"]}",
                modifier = padding(8.dp)
            )
        }
    }
}*/

val database = Firebase.database
val userID = FirebaseAuth.getInstance().uid
val myRef = database.getReference("Users").child("$userID").child("logging").child("Date")
@Composable
fun FirebaseDataDisplay() {
    var data by remember { mutableStateOf<Map<String, Any?>>(emptyMap()) }
    val errorMessage = remember { mutableStateOf("") }
    var showButtonsYear by remember { mutableStateOf(true) }
    var showButtonsDate by remember { mutableStateOf(true) }



    LaunchedEffect(Unit) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    data = snapshot.value as Map<String, Any?>
                } else {
                    errorMessage.value = "No data found"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage.value = "Error fetching data: ${error.message}"
            }
        })
    }

    if (errorMessage.value.isNotEmpty()) {
        Text(text = errorMessage.value)
    } else {
        LazyColumn {
            items(data.entries.toList()) { (value) ->

                //Text(text = "$value")
                Button(onClick = { showButtonsYear = !showButtonsYear }) {
                    Text(if (showButtonsYear)  "$value" else "$value")
                }
                AnimatedVisibility(visible = showButtonsYear) {
                    Column {
                        Button(onClick = { showButtonsDate = !showButtonsDate }) {
                            Text(if (showButtonsDate) "$value" else "$value")
                        }
                        AnimatedVisibility(visible = showButtonsDate) {
                            Column {
                                Button(onClick = { showButtonsDate = !showButtonsDate }) {
                                    Text(if (showButtonsDate) "$value" else "$value")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyScreen() {
    Column {
        FirebaseDataDisplay()
    }
}

@Composable
fun DisplayChildrenButton(modifier: Modifier = Modifier) {
    var children by remember { mutableStateOf<List<DataSnapshot>>(emptyList()) }
    var showYear by remember { mutableStateOf(true) }
    var showDate by remember { mutableStateOf(true) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid
    val databaseRef = database.getReference()
    Column (
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Button(onClick = {
            showYear = !showYear
            databaseRef.child("Users").child("$userID").child("logging").child("Date").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    children = snapshot.children.toList()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }) {

            Text(if (showYear) "2024" else "2024")
        }
        AnimatedVisibility(visible = showYear) {
            children.forEach { child ->
                Row {
                    Button(onClick = {
                        showDate = !showDate
                        databaseRef.child("Users").child("$userID").child("logging").child("Date").child("2024").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                children = snapshot.children.toList()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                    }) {
                        val childKey = child.key
                        val childValue = child.value
                        Text("$childKey")
                    }
                    AnimatedVisibility(visible = showDate) {
                        children.forEach { child ->
                            Row {
                                Button(onClick = {

                                    databaseRef.child("Users").child("$userID").child("logging")
                                        .child("Date").child("2024").child("10-17")
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                children = snapshot.children.toList()
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                // Handle error
                                            }
                                        })
                                }) {
                                    Text("log")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun BoxWithPngBorder() {
//    Box(
//        modifier = Modifier
//            .size(120.dp) // Total size including border (outer Box)
//            .background(Color.Transparent), // Make outer box transparent
//        contentAlignment = Alignment.Center
//    ) {
//        // Inner circular content Box
//        Image(
//            painter = painterResource(id = pro), // Replace with your inner image
//            contentDescription = "Profile Image",
//            modifier = Modifier
//                .size(100.dp) // Size of the inner image, slightly smaller than the outer border
//                .clip(CircleShape),
//            contentScale = ContentScale.Crop // Crop the image to fit in the circular shape
//        )
//
//        // Border overlay using a PNG image
//        Image(
//            painter = painterResource(id = img), // Replace with your circular PNG border
//            contentDescription = "Circular Border Image",
//            modifier = Modifier.size(120.dp), // Size of the border image (slightly larger than content)
//            contentScale = ContentScale.Crop
//        )
//    }
//}

/*@Composable
fun UserLoggingButton() {
    val myRef = database.getReference("Users")
    val userID = FirebaseAuth.getInstance().uid
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
    val monthDayState = remember { mutableStateOf<List<String>?>(null) }
    val loggingDataState = remember { mutableStateOf<List<String>?>(null) }

    Column {
        // Button to load data for the current date
        Button(onClick = {
            // Fetch monthday entries for the current date under the current year
            myRef.child("$userID").child("logging").child("Date").child(currentYear).get()
                .addOnSuccessListener { snapshot ->
                    // Collect monthday children (e.g., each logged day for that month)
                    val monthDayList = snapshot.children.map { it.key ?: "Unnamed Day" }
                    monthDayState.value = monthDayList
                }
        }) {
            Text(text = "Load $currentDate - $currentYear")
        }

        // Display buttons for each monthday under the current year and date
        monthDayState.value?.forEach { day ->
            Button(onClick = {
                // Fetch content for the specific monthday
                myRef.child("Users").child("$userID").child("logging").child(currentDate)
                    .child(currentYear).child(day).get()
                    .addOnSuccessListener { snapshot ->
                        val loggingData = snapshot.children.map { it.value.toString() }
                        loggingDataState.value = loggingData
                    }
            }) {
                Text(text = day)
            }
        }

        // Display the content for the selected monthday
        loggingDataState.value?.forEach { logData ->
            Text(text = logData)
        }
    }
}*/