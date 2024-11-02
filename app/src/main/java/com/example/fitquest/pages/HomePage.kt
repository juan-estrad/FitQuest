package com.example.fitquest.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.R
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlin.system.exitProcess



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
    contents = { HomePageContents(modifier,navController,authViewModel) },
    modifier = modifier,
    navController = navController,
    authViewModel = authViewModel
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
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


    // display content if the userProfile is not null
    userProfile?.let { profile ->


//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(verticalGradientBrush)
//                .padding(30.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ){




//        TopAppBar(
//            colors = topAppBarColors(
//                containerColor = brightOrange,
//                titleContentColor = grayWhite,
//            ),
//
//            title = {
////                Text("Top app bar")
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .background(Color.Gray),
//                    contentAlignment = Alignment.Center
//
//                ) {
//                    Text(profile.username, fontSize = 20.sp, color = Color.White) //profile username
//                }
//            },
//            modifier = Modifier.height(300.dp),
//
//            )




//        Row(
//                modifier = Modifier
//                    .background(verticalGradientBrush)
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//
////                horizontalArrangement = Arrangement.,
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//
//                //Plan is to make the circle the pfp but for now i just put the username in there
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .background(Color.Gray),
//                    contentAlignment = Alignment.Center
//
//                ) {
//                    Text(profile.username, fontSize = 20.sp, color = Color.White) //profile username
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(profile.username, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
//            }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(verticalGradientBrush())
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Row(
//                modifier = Modifier
//
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.Top
//            ){}



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


            Spacer(modifier = Modifier.height(16.dp))
            val database = Firebase.database
            val myRef = database.getReference("Users")

            // Logging page button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable(
                        enabled = authState.value != AuthState.Loading,
                        onClick = { navController.navigate("logging") }
                    )
                    .border(BorderStroke(5.dp, brightOrange), RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp))
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.logpagebtn),
                    contentDescription = "Store Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Text on top of the image
                Text(
                    text = "Log Workout",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            //STORE PAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable(
                        enabled = authState.value != AuthState.Loading,
                        onClick = { navController.navigate("store") }
                    )
                    .border(BorderStroke(5.dp, brightOrange), RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp))
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.storepagebtn),
                    contentDescription = "Log Workout Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Text on top of the image
                Text(
                    text = "Store",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(15.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STATS PAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable(
                        enabled = authState.value != AuthState.Loading,
                        onClick = { navController.navigate("stats") }
                    )
                    .border(BorderStroke(5.dp, brightOrange), RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp))
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.statspagebtn),
                    contentDescription = "Stats page Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Text on top of the image
                Text(
                    text = "Stats",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // FYP BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable(
                        enabled = authState.value != AuthState.Loading,
                        onClick = { navController.navigate("foryou") }
                    )
                    .border(BorderStroke(5.dp, brightOrange), RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp))
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.fypbtn),
                    contentDescription = "FYP Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Text on top of the image
                Text(
                    text = "For You",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(15.dp)
                )
            }


            TextButton(
                onClick = { authViewModel.signout() }) {
                Text(text = "Sign Out", color = Color.Red)
            }




        }
    }


    // This modify the device's back button to function like a the device's home button
    // pressing it will make it minimize the app in stead of shutting it down completely
    BackHandler(enabled = true ) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)


        // THIS FUNCTION BELOW WILL SHUTDOWN THE APP
        // which the app has to be relaunch after

        //exitProcess(1);
    }
}

